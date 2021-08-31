package cl.tenpo.challenge.service;

import cl.tenpo.challenge.exception.UserNotFoundException;
import cl.tenpo.challenge.exception.UsernameAlreadyExistException;
import cl.tenpo.challenge.model.User;
import cl.tenpo.challenge.repository.UserRepository;
import cl.tenpo.challenge.security.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String ERROR_BAD_CREDENTIALS = "Las credenciales de acceso son incorrectas";
    private static final String ERROR_USER_NOT_FOUND = "El usuario %s no existe";


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    @Override
    public User signUpUser(User user) throws UsernameAlreadyExistException {
        log.debug("Init of user creation: " + user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistException(user.getUsername());
        }
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        //user.setValidToken(true);
        return this.userRepository.save(user);

    }

    @Transactional
    @Override
    public String loginUser(String username, String password) throws UserNotFoundException {
        log.debug("Sing in of the: " + username + " has started");
        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

            Authentication result = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            //SecurityContextHolder.getContext().setAuthentication(result);

            user.setHasValidToken(true);
            userRepository.save(user);

            // Se obtiene el UserDetail para generar el token.
            UserDetails userDetails = this.loadUserByUsername(username);

            // Se genera el token.
            final String token = jwtTokenUtil.generateToken(userDetails);

            return token;

        } catch (AuthenticationException ex) {
            throw new BadCredentialsException(ERROR_BAD_CREDENTIALS);
        }
    }

    @Override
    public boolean hasValidToken(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        return user.isHasValidToken();
    }

    @Transactional
    @Override
    public void signOut(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(()-> new UserNotFoundException(username));
        user.setHasValidToken(false);
        userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format(ERROR_USER_NOT_FOUND, username)));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                Collections.emptyList());
    }
}
