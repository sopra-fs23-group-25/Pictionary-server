package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.LobbyRepository;

import ch.uzh.ifi.hase.soprafs23.websockets.dto.UserSocketGetDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class WebSocketServiceTest {

    Lobby testLobby = new Lobby();
    User testUser = new User();
    User test2User = new User();
    @Mock
    private LobbyRepository lobbyRepository;


    @InjectMocks
    private WebSocketService webSocketService;

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
        test2User.setUsername("test2User");
        test2User.setUserId(2L);
        test2User.setLanguage("en");

        testLobby.addPlayer(testUser.convertToPlayer());
        testLobby.addPlayer(test2User.convertToPlayer());

        when(lobbyRepository.save(Mockito.any())).thenReturn(testLobby);
    }

    @Test
    void getUser() throws Exception{
        UserSocketGetDTO testUserSocketGetDTO = new UserSocketGetDTO();
        UserSocketGetDTO test2UserSocketGetDTO = new UserSocketGetDTO();
        List<UserSocketGetDTO> testList = new ArrayList<>();

        testUserSocketGetDTO.setUserId(1L);
        testUserSocketGetDTO.setUsername("testUser");
        testList.add(testUserSocketGetDTO);

        test2UserSocketGetDTO.setUserId(2L);
        test2UserSocketGetDTO.setUsername("test2User");
        testList.add(test2UserSocketGetDTO);

        when(lobbyRepository.findByLobbyId(Mockito.anyLong())).thenReturn(testLobby);
        List<UserSocketGetDTO> resultList = webSocketService.getUsersInLobby(1l);

        assertEquals(testList.get(0).getUserId(), resultList.get(0).getUserId());
        assertEquals(testList.get(0).getUsername(), resultList.get(0).getUsername());
        assertEquals(testList.get(1).getUserId(), resultList.get(1).getUserId());
        assertEquals(testList.get(1).getUsername(), resultList.get(1).getUsername());
    }
}
