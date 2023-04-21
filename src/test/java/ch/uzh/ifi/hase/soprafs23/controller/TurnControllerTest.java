package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessDTO;
import ch.uzh.ifi.hase.soprafs23.service.TurnService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TurnController.class)
public class TurnControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TurnService turnService;

    Turn testTurn = new Turn();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testTurn.setWord("testWord");
        testTurn.setGuesses(new ArrayList<>());
        testTurn.setPainterId(1L);
        testTurn.setCorrectGuesses(0);
        testTurn.setTimePerRound(1L);


    }

    @Test
    public void runTurn_returns_TurnGetDTO() throws Exception {
        given(turnService.initTurn(Mockito.anyLong())).willReturn(testTurn);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated()) // Code 201
                .andExpect(jsonPath("$.word", is(testTurn.getWord())))
                .andExpect(jsonPath("$.painterId").value(testTurn.getPainterId()))
                .andExpect(jsonPath("$.timePerRound").value(testTurn.getTimePerRound()));
    }

    @Test
    public void runTurn_noLobby_throws404() throws Exception {
        given(turnService.initTurn(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void submitGuess_response204() throws Exception {
        GuessDTO guessDTO = new GuessDTO();
        guessDTO.setUserId(1L);

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willReturn(testTurn);


        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent()); // Code 204
    }

    @Test
    public void submitGuess_lobbyNotFound() throws Exception {
        GuessDTO guessDTO = new GuessDTO();
        guessDTO.setUserId(1L);

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound()); // Code 404
    }

    @Test
    public void getResult_returnsTurnGetDTO() throws Exception {
        GuessDTO guessDTO = new GuessDTO();
        guessDTO.setUserId(1L);

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willReturn(testTurn);


        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    public void getResult_LobbyNotFound_returns404() throws Exception {
        GuessDTO guessDTO = new GuessDTO();
        guessDTO.setUserId(1L);

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }



    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}
