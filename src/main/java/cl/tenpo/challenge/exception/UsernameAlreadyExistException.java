package cl.tenpo.challenge.exception;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */
@NoArgsConstructor
@Data
@Getter
public class UsernameAlreadyExistException extends RuntimeException{

    private String username;

    public UsernameAlreadyExistException(String username) {
        this.username = username;
    }
}
