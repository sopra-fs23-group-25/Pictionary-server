package ch.uzh.ifi.hase.soprafs23.controller;


import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.LobbyPostDTO;
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

    @PostMapping("/lobbies")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public LobbyGetDTO createLobby(@RequestBody LobbyPostDTO lobbyPostDTO) {
        Lobby newLobby = DTOMapper.INSTANCE.convertLobbiesPostDTOToEntity(lobbyPostDTO);

        Lobby createdLobby = lobbyService.createLobby(newLobby);

        if(createdLobby == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lobby couldn't be created");
        }
        return DTOMapper.INSTANCE.convertEntityToLobbiesGetDTO(createdLobby);

    }

    @GetMapping("/lobbies")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<LobbyGetDTO> getAllLobbies() {
        // fetch all lobbies in the internal representation
        List<Lobby> lobbies = lobbyService.getLobbies();
        List<LobbyGetDTO> lobbiesGetDTOs = new ArrayList<>();

        // convert each lobby to the API representation
        for (Lobby lobby : lobbies) {
            lobbiesGetDTOs.add(DTOMapper.INSTANCE.convertEntityToLobbiesGetDTO(lobby));
        }
        return lobbiesGetDTOs;
    }

    @PutMapping("/lobbies/{id}/newGame")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGameInLobby(@PathVariable("id") long lobbyId) {
        lobbyService.startGame(lobbyId);
    }

}