package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;

import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;

import ch.uzh.ifi.hase.soprafs23.service.LobbyService;

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

import java.sql.Time;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LobbyController.class)

public class LobbyControllerTest {

    Lobby testLobby = new Lobby();

    Time time = new Time(100L);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LobbyService lobbyService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testLobby.setLobbyId(1L);
        testLobby.setLobbyName("testLobby");
        testLobby.setNrOfRounds(2);
        testLobby.setTimePerRound(time);
        testLobby.setHasStarted(false);
        testLobby.setUsersInLobby(null);
        testLobby.setNumberOfPlayers(0);

    }

    @Test
    public void givenLobbies_whenGetLobbies_thenReturnJSONArray() throws Exception{

        List<Lobby> allLobbies = Collections.singletonList(testLobby);
        given(lobbyService.getLobbies()).willReturn(allLobbies);

        MockHttpServletRequestBuilder getRequest = get("/lobbies").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void givenCorrectInput_whenPostLobby_thenReturnLobby() throws Exception{
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyName("testLobby2");
        lobbyPostDTO.setNrOfRounds(2);
        lobbyPostDTO.setTimePerRound(time);

        given(lobbyService.createLobby(Mockito.any())).willReturn(testLobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyId").value(testLobby.getLobbyId()))
                .andExpect(jsonPath("$.lobbyName", is(testLobby.getLobbyName())))
                .andExpect(jsonPath("$.nrOfRounds", is(testLobby.getNrOfRounds())))
                .andExpect(jsonPath("$.timePerRound", is(testLobby.getTimePerRound().toString())));
    }

    @Test
    public void givenIncorrectInput_whenPostLobby_thenReturnBadRequest() throws Exception{
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyName("testLobby");
        lobbyPostDTO.setNrOfRounds(2);
        lobbyPostDTO.setTimePerRound(time);

        given(lobbyService.createLobby(Mockito.any())).willReturn(null);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void startGame_lobbyNotFound_throws404() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(lobbyService).startGame(anyLong());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/newGame", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    public void startGame_success() throws Exception {
        Mockito.doNothing().when(lobbyService).startGame(anyLong());

        MockHttpServletRequestBuilder putRequest = put("/lobbies/{id}/newGame", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    /**
     * Helper Functions
     */

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
