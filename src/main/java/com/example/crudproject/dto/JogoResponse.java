package com.example.crudproject.dto;

import com.example.crudproject.model.Jogo;

public record JogoResponse(
        Long id,
        String nome,
        String tipo,
        Integer nota,
        String review
) {
    public static JogoResponse from(Jogo jogo) {
        return new JogoResponse(
                jogo.getId(),
                jogo.getNome(),
                jogo.getTipo(),
                jogo.getNota(),
                jogo.getReview()
        );
    }
}
