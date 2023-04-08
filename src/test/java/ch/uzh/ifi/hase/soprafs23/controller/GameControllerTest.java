package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameService gameService;

   Game testGame = new Game();

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        // given
        testGame.setLobbyId(1L);
    }


    @Test
    public void createGame_validInput_gameCreated() throws Exception {

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyId(1L);

        // mocks the userService: for any input the method createUser returns the testUser
        given(gameService.createGame(Mockito.any())).willReturn(testGame);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated()) // Code 201
                .andExpect(jsonPath("$.lobbyId", is(testGame.getLobbyId().intValue())));
    }

    @Test
    public void createGame_noLobby_throws404() throws Exception {

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyId(1L);

        // mocks the userService: for any input the method createUser returns the testUser
        when(gameService.createGame(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound()); // Code 404
    }

    @Test
    public void createGame_GameExists_throws409() throws Exception {

        GamePostDTO gamePostDTO = new GamePostDTO();
        gamePostDTO.setLobbyId(1L);

        // mocks the userService: for any input the method createUser returns the testUser
        when(gameService.createGame(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(gamePostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict()); // Code 409
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
