package cl.tenpo.challenge.service;

import cl.tenpo.challenge.exception.UserNotFoundException;
import cl.tenpo.challenge.exception.UsernameAlreadyExistException;
import cl.tenpo.challenge.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */
public interface UserService extends UserDetailsService {


    User signUpUser(User user) throws UsernameAlreadyExistException;

    String loginUser(String username, String password) throws UserNotFoundException;

    boolean hasValidToken(String username);

    void signOut(String username);
}
