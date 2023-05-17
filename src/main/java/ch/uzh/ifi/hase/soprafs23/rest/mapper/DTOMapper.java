package ch.uzh.ifi.hase.soprafs23.rest.mapper;

import ch.uzh.ifi.hase.soprafs23.entity.*;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

    DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "language", target = "language")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    @Mapping(source = "userId", target = "id")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "language", target = "language")
    UserGetDTO convertEntityToUserGetDTO(User user);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "password", target = "password")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    Session convertSessionPostDTOtoEntity(SessionPostDTO sessionPostDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "language", target = "language")
    SessionGetDTO convertEntityToSessionGetDTO(Session session);

    @Mapping(source = "lobbyId", target = "lobbyId")
    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(source = "nrOfRounds", target = "nrOfRounds")
    @Mapping(source = "maxNrOfPlayers", target = "maxNrOfPlayers")
    @Mapping(source = "timePerRound", target = "timePerRound")
    @Mapping(source = "players", target = "players")
    LobbyGetDTO convertEntityToLobbiesGetDTO(Lobby lobby);

    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(source = "nrOfRounds", target = "nrOfRounds")
    @Mapping(source = "maxNrOfPlayers", target = "maxNrOfPlayers")
    @Mapping(source = "timePerRound", target = "timePerRound")
    @Mapping(source = "hostId", target = "hostId")
    Lobby convertLobbiesPostDTOToEntity(LobbyPostDTO newLobby);

    @Mapping(source="userId", target="userId")
    @Mapping(source="guess", target="guess")
    Guess convertGuessPutDTOToEntity(GuessDTO newGuess);

    @Mapping(source="players", target="players")
    @Mapping(source="wordsPainted", target="wordsPainted")
    @Mapping(source="currentRound", target="currentRound")
    @Mapping(source = "gameOver", target = "gameOver")
    GameGetDTO convertEntityToGameGetDTO (Game game);

    @Mapping(source = "painterId", target = "painterId")
    @Mapping(source = "word", target = "word")
    @Mapping(source = "guesses", target = "guesses")
    TurnGetDTO convertEntityToTurnGetDTO (Turn turn);
}
