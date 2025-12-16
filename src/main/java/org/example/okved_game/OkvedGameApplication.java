package org.example.okved_game;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OkvedGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(OkvedGameApplication.class, args);
    }

}
