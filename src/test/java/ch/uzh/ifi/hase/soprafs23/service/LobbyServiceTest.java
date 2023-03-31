package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.controller.LobbyController;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpStatus;

import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.Time;





public class LobbyServiceTest {

    Lobby testLobby = new Lobby();

    Time time = new Time(100L);

    @Mock
    private LobbyRepository lobbyRepository;

    @InjectMocks
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

        when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);

    }

    @Test
    public void createLobby_validInput_success(){
        Lobby createdLobby = lobbyService.createLobby(testLobby);

        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getLobbyName(), createdLobby.getLobbyName());
        assertEquals(testLobby.getNrOfRounds(), createdLobby.getNrOfRounds());
        assertEquals(testLobby.getTimePerRound(), createdLobby.getTimePerRound());
        assertEquals(testLobby.getNumberOfPlayers(), createdLobby.getNumberOfPlayers());
        assertEquals(testLobby.getUsersInLobby(), createdLobby.getUsersInLobby());


    }

    @Test
    public void createLobby_invalidInput_BadRequest(){
        testLobby.setLobbyName(null);


        Mockito.verify(lobbyRepository, Mockito.times(0)).save(Mockito.any());

        assertThrows(ResponseStatusException.class,()->lobbyService.createLobby(testLobby));


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
