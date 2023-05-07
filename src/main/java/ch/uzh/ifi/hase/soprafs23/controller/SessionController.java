package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.rest.dto.*;
import ch.uzh.ifi.hase.soprafs23.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs23.service.AuthService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {

    private final UserService userService;
    private final AuthService authService;

    SessionController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/sessions")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SessionGetDTO login(@Validated @RequestBody SessionPostDTO sessionPostDTO) {
        Session session = DTOMapper.INSTANCE.convertSessionPostDTOtoEntity(sessionPostDTO);

        Session successfulSession = authService.login(session);

        userService.changeStatus(successfulSession.getUserId(), UserStatus.ONLINE);

        return DTOMapper.INSTANCE.convertEntityToSessionGetDTO(successfulSession);
    }
    @DeleteMapping("/sessions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void logout(@RequestHeader("Auth-Token") String token) {
        long userId = authService.logout(token);

        userService.changeStatus(userId, UserStatus.OFFLINE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
