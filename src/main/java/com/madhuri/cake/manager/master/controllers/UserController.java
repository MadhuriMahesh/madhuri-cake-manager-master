package com.madhuri.cake.manager.master.controllers;

import com.madhuri.cake.manager.master.dao.User;
import com.madhuri.cake.manager.master.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    protected ClientRegistration registration;

    @Autowired
    protected UserRepository userRepository;

    public UserController(ClientRegistrationRepository registrations) {
        this.registration = registrations.findByRegistrationId("facebook");
    }

    /*
    * Login User
    * */
    @GetMapping("/user")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal OAuth2User user) {
        if (user == null) {
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            User userEntity = new User();
            userEntity.setId(String.valueOf(user.getAttributes().get("id")));
            userEntity.setName(String.valueOf(user.getAttributes().get("name")));
            userEntity.setEmail(String.valueOf(user.getAttributes().get("email")));
            userRepository.save(userEntity);
            return ResponseEntity.ok().body(user.getAttributes());
        }
    }

    /*
    * Logout User
    * */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request,
                                    @AuthenticationPrincipal(expression = "idToken") OidcIdToken idToken) {
        // send logout URL to client so they can initiate logout
        String logoutUrl = this.registration.getProviderDetails()
                .getConfigurationMetadata().get("end_session_endpoint").toString();

        Map<String, String> logoutDetails = new HashMap<>();
        logoutDetails.put("logoutUrl", logoutUrl);
        logoutDetails.put("idToken", idToken.getTokenValue());
        request.getSession(false).invalidate();
        return ResponseEntity.ok().body(logoutDetails);
    }
}
