package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    Lobby testLobby = new Lobby();
    Game testGame = new Game();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testLobby.setLobbyId(1L);
        testGame.setLobbyId(1L);
    }

    // POST success - Lobby not found
    @Test
    public void startGame_returnsJSONArray() throws Exception {
        given(gameService.getLobbyByLobbyId(Mockito.anyLong())).willReturn(testLobby);
        given(gameService.newGame(testLobby)).willReturn(testGame);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    // GET: success - lobby not found - game not started

    @Test
    public void getGameOfLobby_returnsJSONArray() throws Exception {
        testLobby.setGame(testGame);
        given(gameService.getLobbyByLobbyId(Mockito.anyLong())).willReturn(testLobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lobbyId").value(testGame.getLobbyId()));
    }

    @Test
    public void startGame_lobbyNotFound_throws404() throws Exception {
        given(gameService.getLobbyByLobbyId(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    public void getGameOfLobby_gameHasNotStarted_throws404 () throws Exception {
        given(gameService.getLobbyByLobbyId(Mockito.anyLong())).willReturn(testLobby);
        given(gameService.newGame(testLobby)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }

    // PUT: success
    @Test
    public void putGame_success() throws Exception {
        doNothing().when(gameService).integrateTurnResults(Mockito.any());
        when(gameService.getGameByLobbyId(Mockito.anyLong())).thenReturn(testGame);

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());

    }

    // DELETE: success
    @Test
    public void deleteGameOfLobby_returns204() throws Exception {
        testLobby.setGame(testGame);
        given(gameService.getLobbyByLobbyId(Mockito.anyLong())).willReturn(testLobby);

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());

    }
}
