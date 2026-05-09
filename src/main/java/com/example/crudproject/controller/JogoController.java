package com.example.crudproject.controller;

import com.example.crudproject.model.Jogo;
import com.example.crudproject.service.JogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class JogoController {

    private final JogoService service;

    public JogoController(JogoService service) {
        this.service = service;
    }

    @GetMapping("/jogos")
    public List<Jogo> listar() {
        return service.listar();
    }

    @GetMapping("/jogos/{id}")
    public ResponseEntity<Jogo> buscar(@PathVariable Long id) {

        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/jogos")
    public ResponseEntity<Jogo> criar(@RequestBody Jogo jogo) {

        Jogo salvo = service.salvar(jogo);

        return ResponseEntity
                .created(URI.create("/jogos/" + salvo.getId()))
                .body(salvo);
    }

    @PutMapping("/jogos/{id}")
    public ResponseEntity<Jogo> atualizar(
            @PathVariable Long id,
            @RequestBody Jogo jogo) {

        return service.buscarPorId(id)
                .map(existente -> {

                    existente.setNome(jogo.getNome());
                    existente.setTipo(jogo.getTipo());
                    existente.setNota(jogo.getNota());
                    existente.setReview(jogo.getReview());

                    Jogo atualizado = service.salvar(existente);

                    return ResponseEntity.ok(atualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/jogos/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}