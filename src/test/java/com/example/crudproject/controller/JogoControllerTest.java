package com.example.crudproject.controller;

import com.example.crudproject.exception.GlobalExceptionHandler;
import com.example.crudproject.exception.JogoNaoEncontradoException;
import com.example.crudproject.model.Jogo;
import com.example.crudproject.service.JogoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JogoController.class)
@Import(GlobalExceptionHandler.class)
class JogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JogoService service;

    @Test
    void deveListarJogos() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new Jogo("Celeste", "Plataforma", 10, "Excelente"),
                new Jogo("Hades", "Roguelike", 9, null)
        ));

        mockMvc.perform(get("/jogos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Celeste"))
                .andExpect(jsonPath("$[1].tipo").value("Roguelike"));
    }

    @Test
    void deveRetornarJogoPorId() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(new Jogo("Portal 2", "Puzzle", 10, "Clássico"));

        mockMvc.perform(get("/jogos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Portal 2"))
                .andExpect(jsonPath("$.nota").value(10));
    }

    @Test
    void deveRetornar404ComCorpoPadronizado() throws Exception {
        when(service.buscarPorId(500L)).thenThrow(new JogoNaoEncontradoException(500L));

        mockMvc.perform(get("/jogos/500"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Jogo não encontrado com o id 500"))
                .andExpect(jsonPath("$.caminho").value("/jogos/500"));
    }

    @Test
    void deveCriarJogoValido() throws Exception {
        Jogo salvo = new Jogo("Minecraft", "Sandbox", 10, "Criativo");
        ReflectionTestUtils.setField(salvo, "id", 12L);
        when(service.criar(any())).thenReturn(salvo);

        String json = objectMapper.writeValueAsString(new Pedido(
                "Minecraft", "Sandbox", 10, "Criativo"
        ));

        mockMvc.perform(post("/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/jogos/12"))
                .andExpect(jsonPath("$.nome").value("Minecraft"));
    }

    @Test
    void deveRejeitarNomeVazio() throws Exception {
        String json = objectMapper.writeValueAsString(new Pedido("", "Ação", 8, null));

        mockMvc.perform(post("/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").value("O nome do jogo é obrigatório"));
    }

    @Test
    void deveRejeitarNotaMaiorQueDez() throws Exception {
        String json = objectMapper.writeValueAsString(new Pedido("Jogo", "Ação", 11, null));

        mockMvc.perform(post("/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nota").value("A nota máxima é 10"));
    }

    @Test
    void deveRejeitarNotaNegativa() throws Exception {
        String json = objectMapper.writeValueAsString(new Pedido("Jogo", "Ação", -1, null));

        mockMvc.perform(post("/jogos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nota").value("A nota mínima é 0"));
    }

    @Test
    void deveAtualizarJogo() throws Exception {
        when(service.atualizar(org.mockito.ArgumentMatchers.eq(8L), any()))
                .thenReturn(new Jogo("Atualizado", "RPG", 9, "Nova review"));
        String json = objectMapper.writeValueAsString(
                new Pedido("Atualizado", "RPG", 9, "Nova review")
        );

        mockMvc.perform(put("/jogos/8")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    void deveExcluirJogo() throws Exception {
        mockMvc.perform(delete("/jogos/4"))
                .andExpect(status().isNoContent());

        verify(service).deletar(4L);
    }

    @Test
    void deveBuscarPorNome() throws Exception {
        when(service.buscarPorNome("mine"))
                .thenReturn(List.of(new Jogo("Minecraft", "Sandbox", 10, null)));

        mockMvc.perform(get("/jogos/buscar").param("nome", "mine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Minecraft"));
    }

    @Test
    void deveBuscarPorTipo() throws Exception {
        when(service.buscarPorTipo("RPG"))
                .thenReturn(List.of(new Jogo("Final Fantasy", "RPG", 9, null)));

        mockMvc.perform(get("/jogos/buscar").param("tipo", "RPG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("RPG"));
    }

    @Test
    void deveBuscarPorNotaMinima() throws Exception {
        when(service.buscarPorNotaMinima(9))
                .thenReturn(List.of(new Jogo("Celeste", "Plataforma", 10, null)));

        mockMvc.perform(get("/jogos/buscar").param("notaMinima", "9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nota").value(10));
    }

    private record Pedido(String nome, String tipo, Integer nota, String review) {
    }
}
