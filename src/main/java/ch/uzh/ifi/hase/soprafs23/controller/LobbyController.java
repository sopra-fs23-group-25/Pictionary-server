package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.LobbyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;


@RestController
public class LobbyController {

    private final LobbyService lobbyService;

    LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    // creates a new lobby
    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {

        // add creator as player to lobby!!
        Lobby newLobby = DTOMapper.INSTANCE.convertLobbiesPostDTOToEntity(lobbyPostDTO);

        Lobby createdLobby = lobbyService.createLobby(newLobby);

        if (createdLobby == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby couldn't be created");
        }
        return DTOMapper.INSTANCE.convertEntityToLobbiesGetDTO(createdLobby);

    }

    // gets all lobbies
    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        // fetch all lobbies in the internal representation
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbiesGetDTOs = new ArrayList<>();

        // convert each lobby to the API representation
        for (Lobby lobby : lobbies) {
            if (!lobby.isRunning() && !lobby.isFull()) {lobbiesGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbiesGetDTO(lobby));}
        }
        return lobbiesGetDTOs;
    }

    @GetMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public LobbyGetDTO getLobby(@PathVariable("lobbyId") long lobbyId) {
        Lobby lobby = lobbyService.getSingleLobby(lobbyId);

        return DTOMapper.INSTANCE.convertEntityToLobbiesGetDTO(lobby);
    }

    @PutMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void joinLobby(@PathVariable("lobbyId") long lobbyId, @RequestBody LobbyPutDTO userToAdd) {
        Lobby lobby = lobbyService.getSingleLobby(lobbyId);
        if (lobby != null) {
            User user = lobbyService.getSingleUser(userToAdd.getUserId());

            Lobby joinedLobby = lobbyService.joinLobby(lobby, user);
            if (joinedLobby == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby couldn't be joined!");
            }
        }
    }

    // gets the game of a lobby specified by lobbyId
    //Using GET lobbies/{lobbyId}/game to retrieve the Game of a Lobby is also more intuitive and easier
    // to understand than GET games/{lobbyId}, since the former directly references the Lobby
    // resource and the sub-resource Game, while the latter implies that you are retrieving a list of Game
    // resources associated with a Lobby ID.

    @DeleteMapping("/lobbies/{lobbyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLobby(@PathVariable("lobbyId") long lobbyId) {
       lobbyService.deleteLobby(lobbyService.getSingleLobby(lobbyId));

    }
}