package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.PlayerRole;
import ch.uzh.ifi.hase.soprafs23.controller.LobbyController;
import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
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
import java.util.ArrayList;
import java.util.List;


public class LobbyServiceTest {

    Lobby testLobby = new Lobby();

    Time time = new Time(100L);

    @Mock
    private LobbyRepository lobbyRepository;
    @Mock
    private GameRepository gameRepository;

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

    @Test
    public void createLobby_conflictingName_throws409(){

        when(lobbyRepository.findByLobbyName(Mockito.any())).thenReturn(testLobby);

        assertThrows(ResponseStatusException.class,()->lobbyService.createLobby(testLobby));
    }

    @Test
    public void getAllLobbies_returnsArrayList() throws Exception {
        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(testLobby);

        when(lobbyRepository.findAll()).thenReturn(lobbies);

        assertEquals(lobbies, lobbyService.getLobbies());
    }

    @Test
    public void getAllLobbies_unsuccessful_throws400() throws Exception {
        when(lobbyRepository.findAll()).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobbies());
    }

    @Test
    public void getSingleLobby_returnsLobby() throws Exception {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        assertEquals(testLobby, lobbyService.getSingleLobby(testLobby.getLobbyId()));
    }

    @Test
    public void getSingleLobby_notFound_throws404() throws Exception {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.getSingleLobby(testLobby.getLobbyId()));
    }

    @Test
    public void startGame_success_changesGameAndLobby() throws Exception{
        Game testGame = new Game();
        testGame.setLobbyId(testLobby.getLobbyId());

        List<User> users = new ArrayList<>();
        User testUser = new User();
        testUser.setUserId(1L);
        testUser.setLanguage("l");
        users.add(testUser);
        testLobby.setUsersInLobby(users);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testGame);


        lobbyService.startGame(testLobby.getLobbyId());

        assertTrue(testLobby.isHasStarted());
        assertEquals(1L, testGame.getPlayers().get(0).getUserId());
        assertEquals("l", testGame.getPlayers().get(0).getLanguage());



    }

    @Test
    public void startGame_lobbyNotFound_throws404() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.startGame(testLobby.getLobbyId()));

    }

    @Test
    public void startGame_GameNotFound_throws404() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(gameRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.startGame(testLobby.getLobbyId()));

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
