package com.madhuri.cake.manager.master.controllers;


import com.madhuri.cake.manager.master.dao.UserRepository;
import com.madhuri.cake.manager.master.utils.TestUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    private ClientRegistration registration;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;


    @Mock
    private UserRepository userRepository;

    private UserController userController;


    @Autowired
    private ClientRegistrationRepository registrations;

    @Autowired
    private WebApplicationContext context;

    private final static String ID_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsIm" +
            "p0aSI6ImQzNWRmMTRkLTA5ZjYtNDhmZi04YTkzLTdjNmYwMzM5MzE1OSIsImlhdCI6MTU0M" +
            "Tk3MTU4MywiZXhwIjoxNTQxOTc1MTgzfQ.QaQOarmV8xEUYV7yvWzX3cUE_4W1luMcWCwpr" +
            "oqqUrg";

    private MockMvc restLogoutMockMvc;


    @Test
    public void getUser() {
        //setup
        ClientRegistrationRepository registrationsMock = Mockito.mock(ClientRegistrationRepository.class);
        userController = new UserController(registrationsMock);
        ReflectionTestUtils.setField(userController, "userRepository", userRepository);
        OAuth2User mockPrincipal = createOAuth2User("name", "name@company.com");
        Mockito.when(userRepository.save(any())).thenReturn(TestUtils.createUser().get());

        //execute
        userController.getUser(mockPrincipal);

        //verify
        verify(userRepository).save(any());
    }


    public static OAuth2User createOAuth2User(String name, String email) {

        Map<String, Object> authorityAttributes = new HashMap<>();
        authorityAttributes.put("key", "value");

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "1234567890");
        attributes.put("name", name);
        attributes.put("email", email);

        return new DefaultOAuth2User(Arrays.asList(authority), attributes, "id");
    }


    /*
     * For this test to run we need
     * @RunWith(SpringRunner.class)
     * @SpringBootTest
     * */
    @Ignore
    @Test
    public void getLogoutInformation() throws Exception {

        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", "ROLE_USER");
        claims.put("sub", 123);
        OidcIdToken idToken = new OidcIdToken(ID_TOKEN, Instant.now(),
                Instant.now().plusSeconds(60), claims);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken(idToken));
        SecurityContextHolderAwareRequestFilter authInjector = new SecurityContextHolderAwareRequestFilter();
        authInjector.afterPropertiesSet();

        this.restLogoutMockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();

        String logoutUrl = this.registrations.findByRegistrationId("oidc").getProviderDetails()
                .getConfigurationMetadata().get("end_session_endpoint").toString();
        restLogoutMockMvc.perform(post("/logout"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.logoutUrl").value(logoutUrl))
                .andExpect(jsonPath("$.idToken").value(ID_TOKEN));
    }

    private OAuth2AuthenticationToken authenticationToken(OidcIdToken idToken) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        OidcUser user = new DefaultOidcUser(authorities, idToken);
        return new OAuth2AuthenticationToken(user, authorities, "oidc");
    }
}