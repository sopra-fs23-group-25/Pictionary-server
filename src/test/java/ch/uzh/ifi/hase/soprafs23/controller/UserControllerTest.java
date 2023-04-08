package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs23.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs23.service.AuthService;
import ch.uzh.ifi.hase.soprafs23.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    // don't delete!!
    @MockBean
    private AuthService authService;
    User testUser = new User();

    String token = "test-token";
    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        // given
        testUser.setUserId(1L);
        testUser.setUsername("testUsername");
        testUser.setToken(token);
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setLanguage("l");
        //testUser.setLobbyId(null);

    }

    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

        List<User> allUsers = Collections.singletonList(testUser);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(userService.getUsers()).willReturn(allUsers);

        // when
        MockHttpServletRequestBuilder getRequest = get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", token);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(testUser.getUsername())))
                .andExpect(jsonPath("$[0].status", is(testUser.getStatus().toString())))
                .andExpect(jsonPath("$[0].language", is(testUser.getLanguage())))
                .andExpect(jsonPath("$[0].id", is(testUser.getUserId().intValue())));
        //Mockito.verify(authService).authUser(token);
    }

    // /users POST status code 201
    // WHAT IS TESTED? for a valid input type (=UserPostDTO), with valid value...
    // 1. does the post request call createUser() ?
    // 2. if createUser() returns a User, does the post request and mapping work such that a valid GetDTO is returned?
    // 3. " , does the post request have response status 201?
    @Test
    public void createUser_validInput_userCreated() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

        // mocks the userService: for any input the method createUser returns the testUser
        given(userService.createUser(Mockito.any())).willReturn(testUser);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated()) // Code 201
                .andExpect(jsonPath("$.id", is(testUser.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.language", is(testUser.getLanguage())))

                .andExpect(jsonPath("$.status", is(testUser.getStatus().toString())));

    }

    // /users POST status code 409
    // WHAT IS TESTED? for a valid input type (=UserPostDTO), but invalid value...
    // 1. does the post request call createUser() ?
    // 2. if createUser() throws a CONFLICT, does the post request have response status code 409?
    @Test
    public void createUser_invalidInput_response409() throws Exception {

        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("testUsername");

        // createUser() is not actually tested, it throws a 409 for any given input
        when(userService.createUser(Mockito.any()))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict()); // Code 409
    }

    // /users/{id} GET status code 200
    // WHAT IS TESTED? for a valid input (=Long), with valid value...
    // 1. does the get request call userService.userById()?
    // 2. Is the path variable {id} with value "testUser.getId().toString()" given as input to userById()?
    // 3. if getUserById(id) returns a User, does the get request and mapping work such that a valid GetDTO is returned?
    // 4. " , does the post request have response status 200?
    @Test
    public void getUser_userExists_returnsUserGetDTO() throws Exception {

        // this mocks the UserService
        // userByID returns testUser for the input testUser.getId()
        // which is given as input to the userById function in the get mapping
        when(userService.userById(testUser.getUserId())).thenReturn(testUser);


        // when
        MockHttpServletRequestBuilder getRequest = get("/users/{id}", testUser.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", token);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk()) // Code 200
                .andExpect(jsonPath("$.id", is(testUser.getUserId().intValue())))
                .andExpect(jsonPath("$.username", is(testUser.getUsername())))
                .andExpect(jsonPath("$.language", is(testUser.getLanguage())))
                .andExpect(jsonPath("$.status", is(testUser.getStatus().toString())));
    }

    // /users/{id} GET status code 404
    // WHAT IS TESTED? for a valid input type (=Long), but invalid value...
    // 1. does the get request call userService.userById()?
    // 2. if getUserById(id) throws a NOT_FOUND, does the post request have response status 404?
    @Test
    public void getUser_userDoesNotExist_response404() throws Exception {

        // this mocks the UserService
        // userByID throws a ResponseStatusException for any input
        when(userService.userById(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when
        MockHttpServletRequestBuilder getRequest = get("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Auth-Token", token);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound()); // Code 404

    }

    // /users/{id} PUT status code 204
    // WHAT IS TESTED? for a valid input type (=UserPutDTO), with valid value...
    // 1. does the put request call userService.userById(id)?
    // 2. Is the path variable {id} with value "testUser.getId().toString()" given as input to userById(id)?
    // 3. is the response status 204 i.e. is no ResponseBody returned?
    // It is not tested whether the testUser's fields are updated
    // as this would be covered by a test for the userService.updateUser()
    @Test
    public void putUser_userExists_response204() throws Exception {

        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedUsername");


        // this mocks the UserService
        // userByID returns testUser for input testUser.getId()
        when(userService.userById(testUser.getUserId())).thenReturn(testUser);

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/" + testUser.getUserId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO))
                .header("Auth-Token", token);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent()); // Code 204
    }

    // /users/{id} PUT status code 404
    // WHAT IS TESTED? for a valid input type (=UserPutDTO), but with invalid value...
    // 1. does the put request call userService.userById(id)?
    // 2. if userById(id) throws a NOT _FOUND, is the response status 404?
    @Test
    public void putUser_userDoesNotExist_response404() throws Exception {
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("updatedUsername");

        // this mocks the UserService
        // userByID throws a ResponseStatusException for any Input (given: 1)
        when(userService.userById(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when
        MockHttpServletRequestBuilder putRequest = put("/users/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userPutDTO))
                .header("Auth-Token", token);

        // then
        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound()); // Code 404
    }




    /**
     * Helper Method to convert userPostDTO into a JSON string such that the input
     * can be processed
     * Input will look like this: {"name": "Test User", "username": "testUsername"}
     *
     * @param object
     * @return string
     */
    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}