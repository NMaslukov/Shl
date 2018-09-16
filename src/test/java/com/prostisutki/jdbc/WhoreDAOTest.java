package com.prostisutki.jdbc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prostisutki.configs.ShlyuxiApplication;
import com.prostisutki.entity.Whore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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

    @Test
    public void save() {
        Whore wh = new Whore();
        wh.setName("yeah");
        wh.setId(22);
        whoreDAO.save(wh);
    }


}