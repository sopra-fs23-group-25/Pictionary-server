package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
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

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser = new User();

    private String token = "testToken";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        // given
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");
        testUser.setToken(token);
        testUser.setStatus(UserStatus.OFFLINE);
        testUser.setLanguage("testLanguage");
        testUser.setPassword("testPassword");
        //testUser.setLobbyId(null);

        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createUser_validInputs_success() {
        // when -> any object is being save in the userRepository -> return the dummy
        // testUser
        User createdUser = userService.createUser(testUser);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testUser.getUserId(), createdUser.getUserId());
        //   assertEquals(testUser.getName(), createdUser.getName());
        assertEquals(testUser.getUsername(), createdUser.getUsername());
        assertNotNull(createdUser.getToken());
        assertEquals(UserStatus.OFFLINE, createdUser.getStatus());
    }

    @Test
    public void createUser_duplicateName_throwsException() {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void createUser_duplicateInputs_throwsException() throws Exception {
        // given -> a first user has already been created
        userService.createUser(testUser);

        // when -> setup additional mocks for UserRepository
        //   Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        // then -> attempt to create second user with same user -> check that an error
        // is thrown
        assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
    }

    @Test
    public void userById_userNotExits() throws Exception {
        assertThrows(ResponseStatusException.class, () -> userService.userById(1L));
    }

    @Test
    public void userById_returnsUser() throws Exception {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));

        assertEquals(testUser, userService.userById(testUser.getUserId()));
    }

    @Test
    public void changeStatus_idNotFound () throws Exception {
        when(userRepository.findById(Mockito.anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        assertThrows(ResponseStatusException.class, () -> userService.changeStatus(1L, UserStatus.ONLINE));
    }

    @Test
    public void changeStatus_ () throws Exception {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));
        userService.changeStatus(1L, UserStatus.ONLINE);
        assertEquals(testUser.getStatus(),UserStatus.ONLINE);

    }

    @Test
    public void updateUser_valid_changesUsername_pWord_language() throws Exception {
        User testUser2 = new User();
        testUser2.setUsername("username2");
        testUser2.setPassword("password2");
        testUser2.setLanguage("language2");

        userService.updateUser(testUser2, testUser);

        assertEquals(testUser2.getUsername(), testUser.getUsername());
        assertEquals(testUser2.getUsername(), testUser.getUsername());
        assertEquals(testUser2.getLanguage(), testUser.getLanguage());
    }

    @Test
    public void updateUser_unchangedValues() throws Exception {
        User testUser2 = new User();
        testUser2.setUsername("testUsername");
        testUser2.setPassword("");
        testUser2.setLanguage("testLanguage");

        userService.updateUser(testUser2, testUser);

        assertEquals(testUser2.getUsername(), testUser.getUsername());
        assertEquals("testUsername", testUser.getUsername());
        assertEquals("testLanguage", testUser.getLanguage());
        assertEquals("testPassword", testUser.getPassword());
    }

    @Test
    public void updateUsername_invalid_throws409() throws Exception {
        User testUser2 = new User();
        when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(testUser2, testUser));
    }

    @Test
    public void getAllUsers_returnsList() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(testUser);
        when(userRepository.findAll()).thenReturn(users);

        assertEquals(users, userService.getUsers());
    }
}