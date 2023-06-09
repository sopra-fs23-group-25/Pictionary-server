package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Image;
import ch.uzh.ifi.hase.soprafs23.entity.Lobby;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.ImageDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {this.gameService = gameService;}

    @PostMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.CREATED)
    public GameGetDTO startGameInLobby(@PathVariable("lobbyId") long lobbyId) {

        Lobby lobby = gameService.getLobbyByLobbyId(lobbyId); // if deleted does not throw not found
        Game game = gameService.newGame(lobby);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @PutMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void endOfTurnUpdate (@PathVariable("lobbyId") long lobbyId) {

        Game game = gameService.getGameByLobbyId(lobbyId);
        gameService.integrateTurnResults(game);
    }

    @PutMapping("/lobbies/{lobbyId}/game/images")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addImage (@PathVariable("lobbyId") long lobbyId,@RequestBody ImageDTO imageDTO) {

        Game game = gameService.getGameByLobbyId(lobbyId);
        int test = imageDTO.getImageData().length();
        Image image = DTOMapper.INSTANCE.convertImageDTOToEntity(imageDTO);
        gameService.addImage(game, image);
    }

    @GetMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGameOfLobby(@PathVariable("lobbyId") long lobbyId) {

        Lobby lobby = gameService.getLobbyByLobbyId(lobbyId);
        Game game = lobby.getGame();

        if (game == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game has not started yet!");
        }

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }

    @DeleteMapping("/lobbies/{lobbyId}/game")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable("lobbyId") long lobbyId) {
        Lobby lobby = gameService.getLobbyByLobbyId(lobbyId);

        gameService.endGame(lobby);
    }
}
