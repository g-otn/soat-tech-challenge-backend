package br.com.grupo63.techchallenge.adapter.driver.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DefaultResponseDTO {
    private String code;
    private String title;
    private String description;
}
