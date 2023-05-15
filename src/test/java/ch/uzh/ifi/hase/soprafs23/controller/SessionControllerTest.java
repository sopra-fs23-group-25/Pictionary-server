package ch.uzh.ifi.hase.soprafs23.controller;

import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.rest.dto.SessionPostDTO;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {


    Session testSession = new Session();

    SessionPostDTO sessionPostDTO = new SessionPostDTO();
    User testUser = new User();

    String token = "testToken";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthService authService;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        // given
        testSession.setUserId(1L);
        testSession.setUsername("testUsername");
        testSession.setToken(token);
        testSession.setPassword("password");

        sessionPostDTO.setPassword(testSession.getPassword());
        sessionPostDTO.setUsername(testSession.getUsername());

        testUser.setUsername("testUser");
        testUser.setUserId(1L);
        testUser.setLanguage("en");

    }

    @Test
    public void login_UsernameNotExist_returns404() throws Exception {

        when(authService.login(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder postRequest = post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionPostDTO))
                .header("Auth-Token", token);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound()); // Code 404

    }


    @Test
    public void login_UsernameNotExist_returns401() throws Exception {

        when(authService.login(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        MockHttpServletRequestBuilder postRequest = post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionPostDTO))
                .header("Auth-Token", token);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isUnauthorized()); // Code 404

    }
    ///does not work for some reason still throws 404

    @Test
    public void loginUser_validCredentials_returnsSessionGetDTO() throws Exception {

        given(authService.login(Mockito.any())).willReturn(testSession);

        Mockito.doNothing().when(userService).changeStatus(anyLong(), Mockito.any());
        when(userService.userById(Mockito.anyLong())).thenReturn(testUser);
        MockHttpServletRequestBuilder postRequest = post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(sessionPostDTO))
                .header("Auth-Token", token);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated()) // Code 201
                .andExpect(jsonPath("$.username", is(testSession.getUsername())))
                .andExpect(jsonPath("$.token", is(testSession.getToken())))
                .andExpect(jsonPath("$.language", is(testSession.getLanguage())));
    }

    @Test
    public void logoutUser_validToken_returns204() throws Exception {

        given(authService.logout(Mockito.any())).willReturn(testSession.getUserId());

        Mockito.doNothing().when(userService).changeStatus(anyLong(), Mockito.any());

        MockHttpServletRequestBuilder deleteRequest = delete("/sessions")
                .header("Auth-Token", token);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent()); // Code 204
    }

    @Test
    public void logoutUser_invalidToken_returns404() throws Exception {

        when(authService.logout(Mockito.any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        MockHttpServletRequestBuilder deleteRequest = delete("/sessions")
                .header("Auth-Token", token);

        // then
        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound()); // Code 204
    }

    // helper method
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
