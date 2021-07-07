package com.madhuri.cake.manager.master.controllers;

import com.google.gson.Gson;
import com.madhuri.cake.manager.master.dao.CakeService;
import com.madhuri.cake.manager.master.dao.UserRepository;
import com.madhuri.cake.manager.master.utils.TestUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CakeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CakeService cakeService;

    @Mock
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @InjectMocks
    private CakeController cakeController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.cakeController).build();
    }

    @Test
    public void getAllCakes() throws Exception {
        //setup
        Mockito.when(cakeService.getAllCakes()).thenReturn(TestUtils.createCakes());

        //execute and verify
        this.mockMvc.perform(get("/allCakes")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
        .andReturn();
    }

    @Test
    public void getCakes() throws Exception {
        //setup
        Mockito.when(cakeService.getAllCakes()).thenReturn(TestUtils.createCakes());

        //execute and verify
        this.mockMvc.perform(get("/")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andReturn();
    }

    @Test
    public void testGetCakes() throws Exception {
        //setup
        Mockito.when(cakeService.findAllByUserId(any())).thenReturn(TestUtils.createCakes());

        Principal mockPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockPrincipal.getName()).thenReturn("me");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cakes")
                .principal(mockPrincipal)
                .accept(MediaType.APPLICATION_JSON);

        //execute and verify
        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andReturn();
    }


    /*
    * For this test to run we need
    * @RunWith(SpringRunner.class)
    * @SpringBootTest
    * */
    @Ignore
    @Test
    @Transactional
    @WithMockUser(username="user", password = "user", authorities = {"ROLE_USER"})
    public void createCake() throws Exception {
        //setup
        Mockito.when(userRepository.findById(any())).thenReturn(TestUtils.createUser());
        Mockito.when(cakeService.createOrUpdateCake(any())).thenReturn(TestUtils.addCake());

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("sub", "test");
        userDetails.put("email", "john.doe@jhipster.com");
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        OAuth2User user = new DefaultOAuth2User(authorities, userDetails, "sub");
        OAuth2AuthenticationToken authentication = new OAuth2AuthenticationToken(user, authorities, "facebook");
        TestSecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(post("/cake")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(TestUtils.addCake())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.login").value("test"))
                .andExpect(jsonPath("$.email").value("john.doe@jhipster.com"))
                .andExpect(jsonPath("$.authorities").value("ROLE_USER"));

    }

    public static OAuth2User createOAuth2User(String name, String email) {

        Map<String, Object> authorityAttributes = new HashMap<>();
        authorityAttributes.put("key", "value");

        GrantedAuthority authority = new SimpleGrantedAuthority("USER");
//        GrantedAuthority authority = new OAuth2UserAuthority(authorityAttributes);


        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "1234567890");
        attributes.put("name", name);
        attributes.put("email", email);

        return new DefaultOAuth2User(Arrays.asList(authority), attributes, "id");
    }

    public static Authentication getOauthAuthenticationFor(OAuth2User principal) {

        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();

        String authorizedClientRegistrationId = "facebook";

        return new OAuth2AuthenticationToken(principal, authorities, authorizedClientRegistrationId);
    }

    @Test
    public void editCake() throws Exception {
        //setup
        Mockito.when(cakeService.createOrUpdateCake(any())).thenReturn(TestUtils.addCake());

        //execute and verify
        this.mockMvc.perform(put("/cake/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(TestUtils.addCake())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn();

    }

    @Test
    public void getCakeByID() throws Exception {
        //setup
        Mockito.when(cakeService.getCakeById(any())).thenReturn(TestUtils.addCake());

        //execute and verify
        this.mockMvc.perform(get("/cake/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn();
    }

    @Test
    public void deleteGroup() throws Exception {
        //execute and verify
        this.mockMvc.perform(delete("/cake/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

}