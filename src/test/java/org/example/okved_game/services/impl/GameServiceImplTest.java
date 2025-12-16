package org.example.okved_game.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.okved_game.dto.FindOkvedResponseDto;
import org.example.okved_game.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceImplTest {
    @Autowired
    private GameService gameService;

    @Test
    void findOkved() throws IOException {
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("exampleOkvedJsonResponse.json");

        String exampleResponse = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        FindOkvedResponseDto result = gameService.findOkved("+79111289224", exampleResponse);
        assertNotEquals("-1", result.getOkvedCode());
    }


    @Test
    void loadOkved() {
        String result = gameService.loadOkved();

        assertNotNull(result);
    }

    @ParameterizedTest
    @MethodSource(value = "getValidPhones")
    void normalizePhone_withValidPhone_returnNormalizePhone(String phone, String expectedPhone) {
        String result = gameService.normalizePhone(phone);

        assertEquals(expectedPhone, result);
    }

    @ParameterizedTest
    @MethodSource(value = "getInvalidPhones")
    void normalizePhone_withInvalidPhone_throwException(String phone) {
        assertThrows(IllegalArgumentException.class, () -> gameService.normalizePhone(phone));
    }

    private static Stream<Arguments> getValidPhones() {
        return Stream.of(
                Arguments.of("89111111111", "+79111111111"),
                Arguments.of("9111111111", "+79111111111"),
                Arguments.of("+89111111111", "+79111111111"),
                Arguments.of("+79111111111", "+79111111111")
        );
    }

    private static Stream<Arguments> getInvalidPhones() {
        return Stream.of(
                Arguments.of("111111111"),
                Arguments.of("++111111111"),
                Arguments.of(""),
                Arguments.of("8911111111s")
        );
    }
}