package org.example.okved_game.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.okved_game.dto.FindOkvedRequestDto;
import org.example.okved_game.dto.FindOkvedResponseDto;
import org.example.okved_game.feign.OkvedFeignClient;
import org.example.okved_game.services.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final OkvedFeignClient okvedFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public FindOkvedResponseDto findOkved(String phone) throws JsonProcessingException {
        String normalizedPhone = normalizePhone(phone);
        String okved = loadOkved();
        return findOkved(normalizedPhone, okved);
    }

    @Override
    public FindOkvedResponseDto findOkved(String normalizedPhone, String okved) throws JsonProcessingException {
        JsonNode okvedJson = objectMapper.readTree(okved);
        return findMaxMatch(normalizedPhone, okvedJson);
    }

    @Override
    public String loadOkved() {
        return okvedFeignClient.getOkved();
    }

    @Override
    public String normalizePhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        if (phone.startsWith("+")) {
            phone = phone.substring(1);
        }
        if (phone.length() == 11) {
            phone = phone.substring(1);
        }
        boolean hasNonDigit = phone.matches(".*\\D.*");
        if (hasNonDigit || phone.length() != 10) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        return "+7" + phone;
    }


    private FindOkvedResponseDto findMaxMatch(String normalizedPhone, JsonNode okvedJson) {
        List<JsonNode> allNodes = new ArrayList<>();
        collectAllNodes(okvedJson, allNodes);

        int maxMatch = 0;
        JsonNode bestMatch = null;

        for (JsonNode node : allNodes) {
            String code = node.get("code").asText().replaceAll("\\.", "");

            int matchLength = matchLength(normalizedPhone, code);

            if (matchLength > maxMatch) {
                maxMatch = matchLength;
                bestMatch = node;
            }
        }

        if (bestMatch == null) {
            return new FindOkvedResponseDto();
        }
        return convertToResponse(bestMatch, normalizedPhone, maxMatch);
    }

    private FindOkvedResponseDto convertToResponse(JsonNode bestMatch, String normalizedPhone, int maxMatch) {
        FindOkvedResponseDto response = new FindOkvedResponseDto();
        response.setOkvedCode(bestMatch.get("code").asText());
        response.setOkvedName(bestMatch.get("name").asText());
        response.setPhone(normalizedPhone);
        response.setMatchCount(maxMatch);
        return response;
    }

    private int matchLength(String normalizedPhone, String code) {
        int i = normalizedPhone.length() - 1;
        int j = code.length() - 1;
        int count = 0;

        while (i >= 0 && j >= 0 && normalizedPhone.charAt(i) == code.charAt(j)) {
            count++;
            i--;
            j--;
        }

        return count;
    }


    private void collectAllNodes(JsonNode nodes, List<JsonNode> result) {
        if (nodes == null) {
            return;
        }

        for (JsonNode node : nodes) {
            collectNode(node, result);
        }
    }

    private void collectNode(JsonNode node, List<JsonNode> result) {
        if (node == null || !node.has("code")) {
            return;
        }

        result.add(node);

        if (node.has("items") && node.get("items").isArray()) {
            for (JsonNode child : node.get("items")) {
                collectNode(child, result);
            }
        }
    }
}
