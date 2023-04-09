package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Game;
import ch.uzh.ifi.hase.soprafs23.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;

    @Autowired
    public GameService(
            @Qualifier("gameRepository") GameRepository gameRepository)
    {
        this.gameRepository = gameRepository;
    }

    public Game createGame (Game game) {

        // check if a game already exists in the lobby and throw a conflict exception if it does
        if (gameRepository.findByLobbyId(game.getLobbyId()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There is already an ongoing game in that lobby!");
        }

        // save only if does not exist yet
        game = gameRepository.save(game);

        return game;
    }

    public Game gameByLobbyId(Long lobbyId) {
        Game game = gameRepository.findByLobbyId(lobbyId);
        if (game == null) {throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Game does not exist!");}

        return game;
    }

    public void deleteGame(Long lobbyId) {

        gameRepository.deleteById(lobbyId);
        if (gameRepository.findByLobbyId(lobbyId) != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Deleting Game was not successful!");
        }
    }

}
