package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Guess;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.TurnService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

@RestController
public class TurnController {

    private final TurnService turnService;

    TurnController(TurnService turnService) {this.turnService = turnService;}

    @PostMapping("/lobbies/{lobbyId}/game/turn")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TurnGetDTO runTurn(@PathVariable("lobbyId") long lobbyId) {
        Turn turn = turnService.initTurn(lobbyId);

        return DTOMapper.INSTANCE.convertEntityToTurnGetDTO(turn);
    }

    @PutMapping("/lobbies/{lobbyId}/game/turn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void submitGuess(@PathVariable("lobbyId") long lobbyId, @RequestBody GuessDTO guessToAdd) {
        Turn turn = turnService.getTurnByLobbyId(lobbyId);

        Guess guess = turnService.addUsername(DTOMapper.INSTANCE.convertGuessPutDTOToEntity(guessToAdd));
        turnService.submitGuess(turn, guess);
    }

    @GetMapping("/lobbies/{lobbyId}/game/turn")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TurnGetDTO getResult(@PathVariable("lobbyId") long lobbyId, @RequestHeader("UserId") long userId) {
        Turn turn = turnService.getTurnByLobbyId(lobbyId);

        turn = turnService.translateTurn(turn, userId);
        return DTOMapper.INSTANCE.convertEntityToTurnGetDTO(turn);
    }

    @DeleteMapping("/lobbies/{lobbyId}/game/turn")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void endTurn(@PathVariable("lobbyId") long lobbyId) {
        turnService.deleteTurn(lobbyId);
    }

}
