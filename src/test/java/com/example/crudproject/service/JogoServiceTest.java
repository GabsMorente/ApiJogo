package com.example.crudproject.service;

import com.example.crudproject.dto.JogoRequest;
import com.example.crudproject.exception.JogoNaoEncontradoException;
import com.example.crudproject.model.Jogo;
import com.example.crudproject.repository.JogoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JogoServiceTest {

    @Mock
    private JogoRepository repository;

    private JogoService service;

    @BeforeEach
    void configurar() {
        service = new JogoService(repository);
    }

    @Test
    void deveListarTodosOsJogos() {
        Jogo primeiro = jogo("Celeste", "Plataforma", 10, "Excelente");
        Jogo segundo = jogo("Hades", "Roguelike", 9, "Muito bom");
        when(repository.findAll()).thenReturn(List.of(primeiro, segundo));

        List<Jogo> resultado = service.listar();

        assertThat(resultado).containsExactly(primeiro, segundo);
        verify(repository).findAll();
    }

    @Test
    void deveBuscarJogoPorId() {
        Jogo jogo = jogo("Hollow Knight", "Metroidvania", 10, null);
        when(repository.findById(7L)).thenReturn(Optional.of(jogo));

        Jogo resultado = service.buscarPorId(7L);

        assertThat(resultado).isSameAs(jogo);
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscarPorId(99L))
                .isInstanceOf(JogoNaoEncontradoException.class)
                .hasMessage("Jogo não encontrado com o id 99");
    }

    @Test
    void deveCriarJogoNormalizandoTextos() {
        JogoRequest request = new JogoRequest(
                "  Stardew Valley  ",
                "  Simulação  ",
                9,
                "  Relaxante e divertido  "
        );
        when(repository.save(org.mockito.ArgumentMatchers.any(Jogo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Jogo resultado = service.criar(request);

        assertThat(resultado.getNome()).isEqualTo("Stardew Valley");
        assertThat(resultado.getTipo()).isEqualTo("Simulação");
        assertThat(resultado.getNota()).isEqualTo(9);
        assertThat(resultado.getReview()).isEqualTo("Relaxante e divertido");
    }

    @Test
    void deveTransformarReviewVaziaEmNulo() {
        JogoRequest request = new JogoRequest("Portal 2", "Puzzle", 10, "   ");
        when(repository.save(org.mockito.ArgumentMatchers.any(Jogo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Jogo resultado = service.criar(request);

        assertThat(resultado.getReview()).isNull();
    }

    @Test
    void deveAtualizarTodosOsCampos() {
        Jogo existente = jogo("Nome antigo", "Ação", 5, "Review antiga");
        JogoRequest request = new JogoRequest("Nome novo", "Aventura", 8, "Review nova");
        when(repository.findById(3L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Jogo resultado = service.atualizar(3L, request);

        assertThat(resultado.getNome()).isEqualTo("Nome novo");
        assertThat(resultado.getTipo()).isEqualTo("Aventura");
        assertThat(resultado.getNota()).isEqualTo(8);
        assertThat(resultado.getReview()).isEqualTo("Review nova");
        verify(repository).save(existente);
    }

    @Test
    void naoDeveSalvarAtualizacaoQuandoIdNaoExiste() {
        JogoRequest request = new JogoRequest("Jogo", "Ação", 8, null);
        when(repository.findById(404L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.atualizar(404L, request))
                .isInstanceOf(JogoNaoEncontradoException.class);
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveExcluirJogoExistente() {
        Jogo jogo = jogo("Limbo", "Puzzle", 8, null);
        when(repository.findById(2L)).thenReturn(Optional.of(jogo));

        service.deletar(2L);

        verify(repository).delete(jogo);
    }

    @Test
    void deveBuscarNomeIgnorandoMaiusculas() {
        Jogo jogo = jogo("Minecraft", "Sandbox", 10, null);
        when(repository.findByNomeContainingIgnoreCase("mine")).thenReturn(List.of(jogo));

        List<Jogo> resultado = service.buscarPorNome("  mine  ");

        assertThat(resultado).containsExactly(jogo);
        verify(repository).findByNomeContainingIgnoreCase("mine");
    }

    @Test
    void deveBuscarTipoIgnorandoMaiusculas() {
        Jogo jogo = jogo("Dead Cells", "Roguelike", 9, null);
        when(repository.findByTipoIgnoreCase("Roguelike")).thenReturn(List.of(jogo));

        List<Jogo> resultado = service.buscarPorTipo(" Roguelike ");

        assertThat(resultado).containsExactly(jogo);
    }

    @Test
    void deveBuscarPorNotaMinima() {
        Jogo jogo = jogo("Ori", "Plataforma", 9, null);
        when(repository.findByNotaGreaterThanEqual(9)).thenReturn(List.of(jogo));

        List<Jogo> resultado = service.buscarPorNotaMinima(9);

        assertThat(resultado).containsExactly(jogo);
    }

    private Jogo jogo(String nome, String tipo, Integer nota, String review) {
        return new Jogo(nome, tipo, nota, review);
    }
}
