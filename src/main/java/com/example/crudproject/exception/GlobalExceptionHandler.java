package com.example.crudproject.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JogoNaoEncontradoException.class)
    public ResponseEntity<ApiError> jogoNaoEncontrado(
            JogoNaoEncontradoException exception,
            HttpServletRequest request) {
        ApiError erro = criarErro(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacao(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, String> campos = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError ->
                campos.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage())
        );

        ApiError erro = criarErro(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos estão inválidos",
                request.getRequestURI(),
                campos
        );
        return ResponseEntity.badRequest().body(erro);
    }

    private ApiError criarErro(
            HttpStatus status,
            String mensagem,
            String caminho,
            Map<String, String> campos) {
        return new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                mensagem,
                caminho,
                campos
        );
    }
}
