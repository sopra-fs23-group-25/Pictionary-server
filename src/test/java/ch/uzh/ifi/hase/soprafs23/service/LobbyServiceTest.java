package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class LobbyServiceTest {

    Lobby testLobby = new Lobby();
    User testUser = new User();
    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
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
        testLobby.setMaxNrOfPlayers(5);
        testLobby.setHostId(1L);

        testUser.setUsername("testUser");
        testUser.setUserId(1L);
        testUser.setLanguage("en");

        when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
    }

    @Test
    public void createLobby_validInput_success() {

        when(lobbyRepository.findByLobbyName(Mockito.any())).thenReturn(null);
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        Lobby createdLobby = lobbyService.createLobby(testLobby);
        Mockito.verify(lobbyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testLobby.getLobbyName(), createdLobby.getLobbyName());
        assertEquals(testLobby.getNrOfRounds(), createdLobby.getNrOfRounds());
        assertEquals(testLobby.getTimePerRound(), createdLobby.getTimePerRound());
        assertEquals(testLobby.getMaxNrOfPlayers(), createdLobby.getMaxNrOfPlayers());
        assertEquals(testLobby.getPlayers(), createdLobby.getPlayers());
        assertEquals(testLobby.getHostId(), createdLobby.getHostId());
    }

    @Test
    public void createLobby_invalidInput_BadRequest() {
        testLobby.setLobbyName(null);

        Mockito.verify(lobbyRepository, Mockito.times(0)).save(Mockito.any());

        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby));
    }

    @Test
    public void createLobby_conflictingName_throws409() {

        when(lobbyRepository.findByLobbyName(Mockito.any())).thenReturn(testLobby);
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> lobbyService.createLobby(testLobby));
    }

    @Test
    public void getAllLobbies_returnsArrayList() {
        List<Lobby> lobbies = new ArrayList<>();
        lobbies.add(testLobby);

        when(lobbyRepository.findAll()).thenReturn(lobbies);

        assertEquals(lobbies, lobbyService.getLobbies());
    }

    @Test
    public void getAllLobbies_unsuccessful_throws400() {
        when(lobbyRepository.findAll()).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class, () -> lobbyService.getLobbies());
    }

    @Test
    public void getSingleLobby_returnsLobby() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        assertEquals(testLobby, lobbyService.getSingleLobby(testLobby.getLobbyId()));
    }

    @Test
    public void getSingleLobby_notFound_throws404() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> lobbyService.getSingleLobby(testLobby.getLobbyId()));
    }

    @Test
    public void joinLobby_success() {
        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        lobbyService.joinLobby(testLobby, testUser);

        assertEquals(testUser.convertToPlayer().getUserId(), testLobby.getPlayers().get(0).getUserId());
    }

    @Test
    public void joinLobby_lobbyFull_returns400() {

        testLobby.setMaxNrOfPlayers(0);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(testLobby,testUser));
    }

    @Test
    public void joinLobby_isRunning_returns400() {

        testLobby.setMaxNrOfPlayers(1);
        testLobby.setRunning(true);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(testLobby,testUser));
    }

    @Test
    public void joinLobby_UserIsAlreadyInLobby_returns409() {
        testLobby.setMaxNrOfPlayers(3);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        when(userRepository.findByUserId(Mockito.anyLong())).thenReturn(testUser);

        lobbyService.joinLobby(testLobby,testUser);

        assertThrows(ResponseStatusException.class, () -> lobbyService.joinLobby(testLobby, testUser));
    }

    @Test
    public void leaveLobby_success_returnsLobby() {

        lobbyService.joinLobby(testLobby, testUser);

        Lobby leftLobby = lobbyService.leaveLobby(testLobby, testUser);

        assertEquals(leftLobby.getLobbyId(), testLobby.getLobbyId());
        assertEquals(0, leftLobby.getPlayers().size());
    }

    @Test
    public void deleteLobby_success(){
        lobbyService.deleteLobby(testLobby);
        assertEquals(1L, testLobby.getLobbyId());
    }
}
