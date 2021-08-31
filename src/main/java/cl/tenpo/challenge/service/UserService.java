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


    /**
     * Crea un usuario con los datos recibidos.
     * @param user
     * @return El usuario creado
     * @throws UsernameAlreadyExistException
     */
    User signUpUser(User user) throws UsernameAlreadyExistException;

    /**
     * Ejecuta el login de usuario. Chequea que las credenciales existan y sean correctas
     * @param username
     * @param password
     * @return Token para ser enviado en los futuros request http
     * @throws UserNotFoundException
     */
    String loginUser(String username, String password) throws UserNotFoundException;

    /**
     * Chequea en Base de Datos que el usuario hay generado un token previamente (es decir, que se haya logueado en el sistema)
     * @param username
     * @return true cuando el token fue generado previamente, false cuando no
     */
    boolean hasValidToken(String username);

    /**
     * Desloguea al usuario del sistema. Marca en base de datos que el usuario ya no tiene generado token
     * @param username
     */
    void signOut(String username);
}
