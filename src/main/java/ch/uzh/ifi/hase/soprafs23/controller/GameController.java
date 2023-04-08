package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GameGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GamePostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
public class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/games")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public GameGetDTO createGame(@RequestBody GamePostDTO gamePostDTO) {
        Game newGame = DTOMapper.INSTANCE.convertGamePostDTOtoEntity(gamePostDTO);
        Game createdGame = gameService.createGame(newGame);
        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(createdGame);
    }

    @GetMapping ("/games/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public GameGetDTO getGame(@PathVariable("id") long lobbyId) {
        Game game = gameService.gameByLobbyId(lobbyId);

        return DTOMapper.INSTANCE.convertEntityToGameGetDTO(game);
    }
}
