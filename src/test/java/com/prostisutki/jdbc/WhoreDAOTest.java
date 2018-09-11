package com.prostisutki.jdbc;

import com.prostisutki.configs.ShlyuxiApplication;
import com.prostisutki.entity.Whore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
@SpringBootTest(classes = ShlyuxiApplication.class)
@RunWith(SpringRunner.class)
public class WhoreDAOTest {

    @Autowired
    WhoreDAO whoreDAO;

    @Test
    public void findById() throws IllegalAccessException, ClassNotFoundException {
        Optional<Whore> byId = whoreDAO.findById(1);
        System.out.println(byId.get());
        System.out.println(whoreDAO.findByIdAndName(2, "pizda").get());
    }
}