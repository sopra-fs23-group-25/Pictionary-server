package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs23.rest.dto.SessionPostDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private AuthService authService;

    private User testUser = new User();

    private String token = "testToken";

    Session testSession = new Session();

    SessionPostDTO sessionPostDTO = new SessionPostDTO();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");
        testUser.setToken(token);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setLanguage("l");

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        when(userRepository.save(Mockito.any())).thenReturn(testUser);
        when(userRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));


        testSession.setUserId(1L);
        testSession.setUsername("testUsername");
        testSession.setToken(token);
        testSession.setPassword("password");

        sessionPostDTO.setPassword(testSession.getPassword());
        sessionPostDTO.setUsername(testSession.getUsername());

    }

    @Test
    public void invalidPassword_throws401 () {
        testUser.setPassword("actualPW");

        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);


        assertThrows(ResponseStatusException.class, () ->
                authService.login(testSession));

    }

    @Test
    public void noUser_throws404 () {
        testUser.setPassword("actualPW");

        when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        assertThrows(ResponseStatusException.class, () ->
                authService.login(testSession));

    }

    @Test
    public void validCredentials_createsSession () {
        testUser.setPassword(testSession.getPassword());

        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        Session createdSession = authService.login(testSession);

        assertEquals(createdSession.getUserId(),testUser.getUserId());
        assertEquals(createdSession.getUsername(),testSession.getUsername());
        assertEquals(createdSession.getToken(),testUser.getToken());
        assertEquals(createdSession.getLanguage(), testUser.getLanguage());
        assertNull(createdSession.getPassword());
    }

    @Test
    public void authUser_invalidToken_throws401 () {

        testUser.setToken("wrongToken"); //token of user not in list of all tokens

        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findAll()).thenReturn(userList);

        assertThrows(ResponseStatusException.class, () ->
                authService.authUser(token));
    }

    @Test
    public void authUserForId_invalidUserId_throws400 () {
        when(userRepository.findById(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));

        assertThrows(ResponseStatusException.class, () -> authService.authUserForUserId(token,testUser.getUserId()));
    }

    @Test
    public void authUserForId_invalidToken_throws401 () {

        testUser.setToken("wrongToken");
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));

        assertThrows(ResponseStatusException.class, () -> authService.authUserForUserId(token,testUser.getUserId()));
    }

    @Test
    public void logoutUser_invalidToken () {
        when(userRepository.findByToken(Mockito.any())).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> authService.logout(token));
    }

    @Test
    public void logoutUser_validToken_returnsUserId () {
        when(userRepository.findByToken(Mockito.any())).thenReturn(testUser);
        assertEquals(authService.logout(token), testUser.getUserId());
    }

}
