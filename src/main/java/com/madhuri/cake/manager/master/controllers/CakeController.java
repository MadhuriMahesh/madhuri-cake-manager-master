package com.madhuri.cake.manager.master.controllers;

import com.madhuri.cake.manager.master.dao.CakeEntity;
import com.madhuri.cake.manager.master.dao.CakeService;
import com.madhuri.cake.manager.master.dao.User;
import com.madhuri.cake.manager.master.dao.UserRepository;
import com.madhuri.cake.manager.master.utility.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
public class CakeController {

    @Autowired
    private CakeService cakeService;

    @Autowired
    private UserRepository userRepository;

    /*
     * Lists the cakes for the user
     * */
    @GetMapping(value = "/cakes")
    public List<CakeEntity> getCakes(Principal principal) {
        List<CakeEntity> cakeEntities = cakeService.findAllByUserId(principal.getName());
        return cakeEntities;
    }

    /*
    * Lists All the cakes
    * */
    @GetMapping(value = "/allCakes")
    public List<CakeEntity> getAllCakes() {
        List<CakeEntity> cakeEntities = cakeService.getAllCakes();
        return cakeEntities;
    }

    /*
     * Lists All the cakes
     * */
    @GetMapping(value = "/")
    public List<CakeEntity> getCakes() {
        List<CakeEntity> cakeEntities = cakeService.getAllCakes();
        return cakeEntities;
    }

    /*
    * Create the cake for the user
    *
    * */
    @PostMapping(value = "/cake")
    public ResponseEntity<CakeEntity> createCake(@RequestBody CakeEntity cakeEntity,
    @AuthenticationPrincipal OAuth2User principal) throws RecordNotFoundException, URISyntaxException {
        log.info("Request to create cake: {}", cakeEntity);
        Map<String, Object> details = principal.getAttributes();
        String userId = details.get("id").toString();

        // check to see if user already exists
        Optional<User> user = userRepository.findById(userId);
        cakeEntity.setUser(user.orElse(new User(userId,
                details.get("name").toString(), details.get("email").toString())));
        CakeEntity responseCakeEntity = cakeService.createOrUpdateCake(cakeEntity);
        return ResponseEntity.created(new URI("/cake/" + responseCakeEntity.getId()))
                .body(responseCakeEntity);
    }

    /*
    * Edit the cake
    * */
    @PutMapping(value = "/cake/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CakeEntity> editCake(@RequestBody CakeEntity cakeEntity) throws RecordNotFoundException {
        log.info("Request to update cake: {}", cakeEntity);
        CakeEntity responseCakeEntity = cakeService.createOrUpdateCake(cakeEntity);
        return ResponseEntity.ok().body(responseCakeEntity);
    }

    /*
    * Get the cake by ID
    * */
    @GetMapping(value = "/cake/{id}")
    public ResponseEntity<CakeEntity> getCakeByID(@PathVariable(value = "id") Integer id) throws RecordNotFoundException {
        CakeEntity responseCakeEntity = cakeService.getCakeById(Long.valueOf(id));
        return ResponseEntity.ok().body(responseCakeEntity);
    }

    /*
    *
    * Delete the cake by Id
    * */
    @DeleteMapping("/cake/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) throws RecordNotFoundException {
        log.info("Request to delete cake: {}", id);
        cakeService.deleteCakeById(id);
        return ResponseEntity.ok().build();
    }


}
