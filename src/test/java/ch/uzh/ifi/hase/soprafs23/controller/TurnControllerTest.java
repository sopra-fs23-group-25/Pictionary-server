package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessGetDTO;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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
    }

    // POST: success (201) - noLobby (404)
    @Test
    public void postTurn_returns_TurnGetDTO() throws Exception {
        given(turnService.initTurn(Mockito.anyLong())).willReturn(testTurn);

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated()) // Code 201
                .andExpect(jsonPath("$.word", is(testTurn.getWord())))
                .andExpect(jsonPath("$.painterId").value(testTurn.getPainterId()));
    }

    @Test
    public void postTurn_noLobby_throws404() throws Exception {
        given(turnService.initTurn(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = post("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound());
    }

    // PUT: success (204) - noLobby(404) - player has already guessed throws (409) - translator interrupts
    @Test
    public void submitGuess_response204() throws Exception {
        GuessGetDTO guessGetDTO = new GuessGetDTO();
        guessGetDTO.setUsername("name");

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willReturn(testTurn);


        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessGetDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent()); // Code 204
    }

    @Test
    public void submitGuess_lobbyNotFound() throws Exception {
        GuessGetDTO guessGetDTO = new GuessGetDTO();
        guessGetDTO.setUsername("name");

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessGetDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound()); // Code 404
    }

    @Test
    public void submitGuess_PlayerAlreadyGuessed_409() throws Exception {
        GuessGetDTO guessGetDTO = new GuessGetDTO();
        guessGetDTO.setUsername("name");

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessGetDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isConflict()); // Code 409
    }

    /*@Test
    public void submitGuess_translatorInterrupt_responds500() throws Exception {
        GuessGetDTO guessGetDTO = new GuessGetDTO();
        guessGetDTO.setUsername("name");

        doThrow(new InterruptedException()).when(turnService).verifyGuess(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(guessGetDTO));

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isInternalServerError()); // Code 500
    }*/

    // GET: success - lobbyNotFound (404)
    @Test
    public void getTurn_returnsTurnGetDTO() throws Exception {
        GuessGetDTO guessGetDTO = new GuessGetDTO();
        guessGetDTO.setUsername("name");

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willReturn(testTurn);


        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void getResult_LobbyNotFound_responds404() throws Exception {
        GuessGetDTO guessGetDTO = new GuessGetDTO();
        guessGetDTO.setUsername("name");

        given(turnService.getTurnByLobbyId(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    // DELETE
    @Test
    public void deleteTurn_exists() throws Exception {
        doNothing().when(turnService).deleteTurn(Mockito.anyLong());

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{id}/game/turn", 1)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
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
