package org.example.okved_game.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.okved_game.dto.FindOkvedRequestDto;
import org.example.okved_game.dto.FindOkvedResponseDto;

public interface GameService {

    FindOkvedResponseDto findOkved(String phone) throws JsonProcessingException;

    FindOkvedResponseDto findOkved(String normalizedPhone, String okved) throws JsonProcessingException;

    String loadOkved();

    String normalizePhone(String phone);
}
