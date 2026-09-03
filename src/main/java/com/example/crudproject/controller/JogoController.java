package com.example.crudproject.controller;

import com.example.crudproject.dto.JogoRequest;
import com.example.crudproject.dto.JogoResponse;
import com.example.crudproject.service.JogoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/jogos")
public class JogoController {

    private final JogoService service;

    public JogoController(JogoService service) {
        this.service = service;
    }

    @GetMapping
    public List<JogoResponse> listar() {
        return service.listar().stream().map(JogoResponse::from).toList();
    }

    @GetMapping("/{id}")
    public JogoResponse buscar(@PathVariable Long id) {
        return JogoResponse.from(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<JogoResponse> criar(@Valid @RequestBody JogoRequest request) {
        var salvo = service.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();
        return ResponseEntity.created(location).body(JogoResponse.from(salvo));
    }

    @PutMapping("/{id}")
    public JogoResponse atualizar(
            @PathVariable Long id,
            @Valid @RequestBody JogoRequest request) {
        return JogoResponse.from(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    public List<JogoResponse> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Integer notaMinima) {
        if (nome != null && !nome.isBlank()) {
            return converter(service.buscarPorNome(nome));
        }
        if (tipo != null && !tipo.isBlank()) {
            return converter(service.buscarPorTipo(tipo));
        }
        if (notaMinima != null) {
            return converter(service.buscarPorNotaMinima(notaMinima));
        }
        return converter(service.listar());
    }

    private List<JogoResponse> converter(List<com.example.crudproject.model.Jogo> jogos) {
        return jogos.stream().map(JogoResponse::from).toList();
    }
}
