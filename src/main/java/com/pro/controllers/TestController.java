package com.pro.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String test(){
        return "zalupa";
    }

}
//./mvnw package && java -jar target/Shala-0.0.1-SNAPSHOT.jar
//docker run -p 8079:8079 -t target/Shala-0.0.1-SNAPSHOT.jar

