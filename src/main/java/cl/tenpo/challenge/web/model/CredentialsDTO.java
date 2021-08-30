package cl.tenpo.challenge.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@AllArgsConstructor
@Data
public class CredentialsDTO {

    @NotBlank(message = "el campo username es obligatorio!")
    private String username;

    @NotBlank(message = "El campo password es obligatorio!")
    private String password;
}
