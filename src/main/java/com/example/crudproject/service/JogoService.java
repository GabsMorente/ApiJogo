package com.example.crudproject.service;

import com.example.crudproject.model.Jogo;
import com.example.crudproject.repository.JogoRepository;
import com.example.crudproject.dto.JogoRequest;
import com.example.crudproject.exception.JogoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JogoService {

    private final JogoRepository repository;

    public JogoService(JogoRepository repository) {
        this.repository = repository;
    }

    public List<Jogo> listar() {
        return repository.findAll();
    }

    public Jogo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new JogoNaoEncontradoException(id));
    }

    @Transactional
    public Jogo criar(JogoRequest request) {
        Jogo jogo = new Jogo(
                request.nome().trim(),
                request.tipo().trim(),
                request.nota(),
                normalizarReview(request.review())
        );
        return repository.save(jogo);
    }

    @Transactional
    public Jogo atualizar(Long id, JogoRequest request) {
        Jogo jogo = buscarPorId(id);
        jogo.setNome(request.nome().trim());
        jogo.setTipo(request.tipo().trim());
        jogo.setNota(request.nota());
        jogo.setReview(normalizarReview(request.review()));
        return repository.save(jogo);
    }

    @Transactional
    public void deletar(Long id) {
        Jogo jogo = buscarPorId(id);
        repository.delete(jogo);
    }

    public List<Jogo> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }

    public List<Jogo> buscarPorTipo(String tipo) {
        return repository.findByTipoIgnoreCase(tipo.trim());
    }

    public List<Jogo> buscarPorNotaMinima(Integer notaMinima) {
        return repository.findByNotaGreaterThanEqual(notaMinima);
    }

    private String normalizarReview(String review) {
        if (review == null || review.isBlank()) {
            return null;
        }
        return review.trim();
    }
}
