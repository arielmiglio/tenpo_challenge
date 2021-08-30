package cl.tenpo.challenge.web.model;

import cl.tenpo.challenge.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private long id;
    private String username;
}
