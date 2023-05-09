package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.entity.Session;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authUserForUserId(String inputToken, long userId) {
        String token = userRepository
                .findById(userId)
                .map(User::getToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid user id %s", userId)));

        if (!token.equals(inputToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    public void authUser(String token) {

        Set<String> allTokens = userRepository
                .findAll()
                .stream()
                .map(User::getToken)
                .collect(Collectors.toSet());

        if (!allTokens.contains(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private void checkIfValidPassword(Session session) {
        User userByUsername = userRepository.findByUsername(session.getUsername());
        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No user with that username!");}

        String inputPW = session.getPassword();
        String actualPW = userByUsername.getPassword();

        if (!inputPW.equals(actualPW)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Password incorrect!");
        }
    }

    public Session login(Session session) {

        User user = userRepository.findByUsername(session.getUsername());

        checkIfValidPassword(session);

        return new Session(
                user.getUsername(),
                null,
                user.getUserId(),
                user.getToken(),
                user.getLanguage()
        );
    }

    public long logout(String token) {
        User userByToken = userRepository.findByToken(token);
        if (userByToken == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Token not found!");}

        return userByToken.getUserId();
    }
}

