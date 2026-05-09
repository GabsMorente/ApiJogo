package com.example.crudproject.service;

import com.example.crudproject.model.Jogo;
import com.example.crudproject.repository.JogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JogoService {

    private final JogoRepository repository;

    public JogoService(JogoRepository repository) {
        this.repository = repository;
    }

    public List<Jogo> listar() {
        return repository.findAll();
    }

    public Optional<Jogo> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Jogo salvar(Jogo jogo) {
        return repository.save(jogo);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}