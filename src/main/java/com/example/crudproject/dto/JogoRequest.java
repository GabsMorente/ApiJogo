package com.example.crudproject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record JogoRequest(
        @NotBlank(message = "O nome do jogo é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "O tipo do jogo é obrigatório")
        @Size(max = 50, message = "O tipo deve ter no máximo 50 caracteres")
        String tipo,

        @NotNull(message = "A nota é obrigatória")
        @Min(value = 0, message = "A nota mínima é 0")
        @Max(value = 10, message = "A nota máxima é 10")
        Integer nota,

        @Size(max = 1000, message = "A review deve ter no máximo 1000 caracteres")
        String review
) {
}
