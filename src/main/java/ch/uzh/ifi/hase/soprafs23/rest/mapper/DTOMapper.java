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

    // POST /users
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "language", target = "language")
    User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

    // GET /users/{userId}
    @Mapping(source = "userId", target = "id") //where is target? why can i not change it to "userId"?
    @Mapping(source = "username", target = "username")
    @Mapping(source = "lobbyId", target = "lobbyId")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "token", target = "token")
    UserGetDTO convertEntityToUserGetDTO(User user);

    // PUT /users/{userId}
    @Mapping(source = "username", target = "username")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "password", target = "password")
    User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

    // POST /sessions
    @Mapping(source = "username", target = "username")
    @Mapping(source = "password", target = "password")
    Session convertSessionPostDTOtoEntity(SessionPostDTO sessionPostDTO);

    @Mapping(source = "token", target = "token")
    @Mapping(source = "username", target = "username")
    @Mapping(source = "userId", target = "userId")
    SessionGetDTO convertEntityToSessionGetDTO(Session session);

    @Mapping(source = "lobbyId", target = "lobbyId")
    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(source = "nrOfRounds", target = "nrOfRounds")
    @Mapping(source = "numberOfPlayers", target = "numberOfPlayers")
    @Mapping(source = "timePerRound", target = "timePerRound")

    LobbyGetDTO convertEntityToLobbiesGetDTO(Lobby lobby);

    @Mapping(source = "lobbyName", target = "lobbyName")
    @Mapping(source = "nrOfRounds", target = "nrOfRounds")
    @Mapping(source = "timePerRound", target = "timePerRound")
    Lobby convertLobbiesPostDTOToEntity(LobbyPostDTO newLobby);

    @Mapping(source="userId", target="userId")
    @Mapping(source="guess", target="guess")
    @Mapping(source="time", target="time")
    @Mapping(source="score", target="score")
    Guess convertGuessPostDTOToEntity(GuessPostDTO newGuess);

    @Mapping(source="lobbyId", target="lobbyId")
    Game convertGamePostDTOtoEntity(GamePostDTO gamePostDTO);

    @Mapping(source="lobbyId", target="lobbyId")
    GameGetDTO convertEntityToGameGetDTO (Game game);

}
