package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.entity.Player;
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
            if (!lobby.isHasStarted()) {lobbiesGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbiesGetDTO(lobby));}
        }
        return lobbiesGetDTOs;
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
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby couldn't be joined");
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby couldn't be found.");
        }
    }

    // gets the game of a lobby specified by lobbyId
    //Using GET lobbies/{lobbyId}/game to retrieve the Game of a Lobby is also more intuitive and easier
    // to understand than GET games/{lobbyId}, since the former directly references the Lobby
    // resource and the sub-resource Game, while the latter implies that you are retrieving a list of Game
    // resources associated with a Lobby ID.
    @GetMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameOfLobby(@PathVariable("lobbyId") long lobbyId) {

        Lobby lobby = lobbyService.getSingleLobby(lobbyId);
        Game game = lobby.getGame();

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game has not started yet!");
        }

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PostMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.CREATED)
    public GameGetDTO startGameInLobby(@PathVariable("lobbyId") long lobbyId) {

        Lobby lobby = lobbyService.getSingleLobby(lobbyId); // if deleted does not throw not found
        Game game = lobbyService.newGame(lobby);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @DeleteMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("lobbyId") long lobbyId) {
        Lobby lobby = lobbyService.getSingleLobby(lobbyId);

        lobbyService.endGame(lobby);

    }

}