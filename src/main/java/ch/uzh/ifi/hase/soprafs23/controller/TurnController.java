package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.entity.Guess;
import ch.uzh.ifi.hase.soprafs23.entity.Turn;
import ch.uzh.ifi.hase.soprafs23.rest.dto.GuessDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.TurnGetDTO;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.GameService;
import ch.uzh.ifi.hase.soprafs23.service.TurnService;
import ch.uzh.ifi.hase.soprafs23.websockets.dto.MessageRelayDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class TurnController {

    private final TurnService turnService;
    private final GameService gameService;

    private final WebSocketController webSocketController;

    TurnController(TurnService turnService, GameService gameService, WebSocketController webSocketController) {
        this.turnService = turnService;
        this.gameService = gameService;
        this.webSocketController = webSocketController;
    }

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

        boolean allGuessed = turnService.everyPlayerGuessed(turn);

        if (allGuessed){
            Game game = gameService.getGameByLobbyId(lobbyId);
            gameService.integrateTurnResults(game);
            MessageRelayDTO message = new MessageRelayDTO();
            if(game.getGameOver()){
                message.setTask("end last round");
            } else{
                message.setTask("end round");
            }
            webSocketController.sendGameState(message, lobbyId);

        }

        // used this to test with postman, throws exception as soon as last guess is submitted:
        // if (allGuessed) {throw new RuntimeException("all Players guessed");}

        // here To-Do: respond with websocket -> end turn
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