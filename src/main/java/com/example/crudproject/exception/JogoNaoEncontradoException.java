package com.example.crudproject.exception;

public class JogoNaoEncontradoException extends RuntimeException {

    public JogoNaoEncontradoException(Long id) {
        super("Jogo não encontrado com o id " + id);
    }
}
