package ch.uzh.ifi.hase.soprafs23.service;

import ch.uzh.ifi.hase.soprafs23.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs23.entity.User;
import ch.uzh.ifi.hase.soprafs23.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        //newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        checkIfUsernameTaken(newUser.getUsername());
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUser(User userWithUpdateInfo, User userToUpdate) {
        if (!userToUpdate.getUsername().equals(userWithUpdateInfo.getUsername())) {
            checkIfUsernameTaken(userWithUpdateInfo.getUsername());
            userToUpdate.setUsername(userWithUpdateInfo.getUsername());
        }
        if(userWithUpdateInfo.getPassword() != null) {
            userToUpdate.setPassword(userWithUpdateInfo.getPassword());
        }
        if(userWithUpdateInfo.getLanguage() != null) {
            userToUpdate.setLanguage(userWithUpdateInfo.getLanguage());
        }

        userRepository.save(userToUpdate);
        userRepository.flush();
    }

    private void checkIfUsernameTaken(String username) {
        User userByUsername = userRepository.findByUsername(username);
        String baseErrorMessage = "The %s provided %s not unique.";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
        }
    }

    public User userById(Long userId) {
        Optional<User> userById = userRepository.findById(userId);
        if (userById.isPresent()) {
            return userById.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "UserId not found!");
        }
    }

    public void changeStatus(long userId, UserStatus status) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        user.setStatus(status);
        userRepository.save(user);
    }

}
