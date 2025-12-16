package org.example.okved_game.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindOkvedResponseDto {
    private String phone;
    private String okvedCode;
    private String okvedName;
    private int matchCount;
}
