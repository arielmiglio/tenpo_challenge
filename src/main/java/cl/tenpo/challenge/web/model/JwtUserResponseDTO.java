package cl.tenpo.challenge.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author Ariel Miglio
 * @date 22/8/2021
 */
@Data
@Builder
@AllArgsConstructor
public class JwtUserResponseDTO {

    private String username;
    private String token;

}
