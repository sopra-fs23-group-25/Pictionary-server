package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;

import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;

import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPutDTO;
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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LobbyController.class)

public class LobbyControllerTest {

    Lobby testLobby = new Lobby();
    Game testGame = new Game();

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
        testLobby.setTimePerRound(60L);
        testLobby.setRunning(false);
        testLobby.setPlayers(new ArrayList<>());
        testLobby.setMaxNrOfPlayers(1);

        testGame.setLobbyId(1L);

    }

    @Test
    public void givenLobbies_whenGetLobbies_thenReturnJSONArray() throws Exception{

        List<Lobby> allLobbies = new ArrayList<>();
        allLobbies.add(testLobby);
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
        lobbyPostDTO.setTimePerRound(60L);

        given(lobbyService.createLobby(Mockito.any())).willReturn(testLobby);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lobbyId").value(testLobby.getLobbyId()))
                .andExpect(jsonPath("$.lobbyName", is(testLobby.getLobbyName())))
                .andExpect(jsonPath("$.nrOfRounds", is(testLobby.getNrOfRounds())))
                .andExpect(jsonPath("$.timePerRound").value(testLobby.getTimePerRound()));
    }

    @Test
    public void getLobby_exists() throws Exception{
        given(lobbyService.getSingleLobby(Mockito.anyLong())).willReturn(testLobby);

        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk());
    }

    @Test
    public void deleteLobby_exists() throws Exception {
        given(lobbyService.getSingleLobby(Mockito.anyLong())).willReturn(testLobby);

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest).andExpect(status().isNoContent());
    }

    @Test
    public void givenCorrectInput_whenPutLobby_thenReturnSucess() throws Exception{
        LobbyPutDTO lobbyPutDTO = new LobbyPutDTO();
        lobbyPutDTO.setUserId(1L);

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setUserId(1L);
        testUser.setLanguage("en");

        given(lobbyService.getSingleLobby(Mockito.anyLong())).willReturn(testLobby);
        given(lobbyService.getSingleUser(Mockito.anyLong())).willReturn(testUser);
        given(lobbyService.joinLobby(Mockito.any(), Mockito.any())).willReturn(testLobby);
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isOk());
    }

    @Test
    public void givenCorrectInput_whenPutLobby_lobbyDoesNotExists_throws400() throws Exception{
        LobbyPutDTO lobbyPutDTO = new LobbyPutDTO();
        lobbyPutDTO.setUserId(1L);

        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setUserId(1L);
        testUser.setLanguage("en");

        given(lobbyService.getSingleLobby(Mockito.anyLong())).willReturn(testLobby);
        given(lobbyService.getSingleUser(Mockito.anyLong())).willReturn(testUser);
        given(lobbyService.joinLobby(Mockito.any(), Mockito.any())).willReturn(null);
        MockHttpServletRequestBuilder putRequest = put("/lobbies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPutDTO));

        mockMvc.perform(putRequest).andExpect(status().isBadRequest());
    }

    @Test
    public void givenIncorrectInput_whenPostLobby_thenReturnBadRequest() throws Exception{
        LobbyPostDTO lobbyPostDTO = new LobbyPostDTO();
        lobbyPostDTO.setLobbyName("testLobby");
        lobbyPostDTO.setNrOfRounds(2);
        lobbyPostDTO.setTimePerRound(60L);

        given(lobbyService.createLobby(Mockito.any())).willReturn(null);

        MockHttpServletRequestBuilder postRequest = post("/lobbies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(lobbyPostDTO));

        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getGameOfLobby_LobbyNotExists_throws404 () throws Exception {
        given(lobbyService.getSingleLobby(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder getRequest = get("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteGameOfLobby_LobbyNotExists_throws404 () throws Exception {
        given(lobbyService.getSingleLobby(Mockito.anyLong())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder deleteRequest = delete("/lobbies/{id}/game", 1)
                .contentType(MediaType.APPLICATION_JSON); //?

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());

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
