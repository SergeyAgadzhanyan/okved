package org.example.okved_game.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.okved_game.dto.FindOkvedResponseDto;
import org.example.okved_game.services.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class OkvedController {
    private final GameService gameService;

    @GetMapping("/okved")
    public FindOkvedResponseDto findOkved(@RequestParam String phone)
            throws JsonProcessingException {
        return gameService.findOkved(phone);
    }
}
