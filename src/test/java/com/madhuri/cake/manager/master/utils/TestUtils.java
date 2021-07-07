package com.madhuri.cake.manager.master.utils;

import com.madhuri.cake.manager.master.dao.CakeEntity;
import com.madhuri.cake.manager.master.dao.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestUtils {

    public static List<CakeEntity> createCakes() {
        List<CakeEntity> cakeEntities = new ArrayList<>();
        CakeEntity cakeEntity1 = new CakeEntity();
        cakeEntity1.setId(1L);
        cakeEntity1.setTitle("cake1");
        cakeEntity1.setDescription("desc1");
        cakeEntity1.setImage("img1");

        cakeEntities.add(cakeEntity1);

        CakeEntity cakeEntity2 = new CakeEntity();
        cakeEntity2.setId(2L);
        cakeEntity2.setTitle("cake2");
        cakeEntity2.setDescription("desc2");
        cakeEntity2.setImage("img2");
        cakeEntities.add(cakeEntity2);

        return cakeEntities;
    }

    public static CakeEntity addCake() {
        CakeEntity cakeEntity1 = new CakeEntity();
        cakeEntity1.setId(1L);
        cakeEntity1.setTitle("cake1");
        cakeEntity1.setDescription("desc1");
        cakeEntity1.setImage("img1");

        return cakeEntity1;
    }

    public static Optional<User> createUser() {
        User user = new User();
        user.setId("123");
        user.setName("name");
        user.setEmail("name@company.com");

        return Optional.of(user);
    }

}
