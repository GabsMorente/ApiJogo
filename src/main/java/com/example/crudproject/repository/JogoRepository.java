package com.example.crudproject.repository;

import com.example.crudproject.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JogoRepository extends JpaRepository<Jogo, Long> {

    List<Jogo> findByNomeContainingIgnoreCase(String nome);

    List<Jogo> findByTipoIgnoreCase(String tipo);

    List<Jogo> findByNotaGreaterThanEqual(Integer notaMinima);
}
