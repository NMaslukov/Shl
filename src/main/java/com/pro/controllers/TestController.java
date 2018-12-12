package com.pro.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class TestController {

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public String test(HttpServletRequest request) throws IOException {
        String test = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        System.out.println(test);
        return "zalupa";
    }

}
//./mvnw package && java -jar target/Shala-0.0.1-SNAPSHOT.jar
//docker run -p 8079:8079 -t target/Shala-0.0.1-SNAPSHOT.jar

