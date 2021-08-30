package cl.tenpo.challenge.exception;

import lombok.Data;
import lombok.Getter;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@Data
@Getter
public class UserNotFoundException extends RuntimeException{

    private String username;

    public UserNotFoundException(String username) {
        this.username = username;
    }
}
