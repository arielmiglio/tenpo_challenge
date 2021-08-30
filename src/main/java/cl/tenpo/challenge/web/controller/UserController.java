package cl.tenpo.challenge.web.controller;

import cl.tenpo.challenge.model.User;
import cl.tenpo.challenge.security.JwtTokenUtil;
import cl.tenpo.challenge.service.UserService;
import cl.tenpo.challenge.web.model.CredentialsDTO;
import cl.tenpo.challenge.web.model.JwtUserResponseDTO;
import cl.tenpo.challenge.web.model.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@RestController()
@RequestMapping("/api/v1/user")
@Slf4j
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Operation(summary = "Crea un nuevo usuario")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Ocurrió un error al realizar la operación"),
                            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
                            @ApiResponse(responseCode = "422", description = "El nombre de usuario ya existe")})
    @SecurityRequirements

    @PostMapping("/signup")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO signUp(@Valid @RequestBody CredentialsDTO credentialsDTO){
        User createdUser = userService.signUpUser(User.builder()
                .username(credentialsDTO.getUsername())
                .password(credentialsDTO.getPassword())
                .build());
        return new UserDTO(createdUser.getId(), createdUser.getUsername());
    }

    @Operation(summary = "Acceso del usuario al sistema. Retorna un token cuando es satisfactorio")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Ocurrió un error al realizar la operación"),
                            @ApiResponse(responseCode = "200", description = "El Usuario accedió exitosamente"),
                            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")})
    @SecurityRequirements

    @PostMapping("/login")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public JwtUserResponseDTO login(@Valid @RequestBody CredentialsDTO credentialsDTO){
        // Se valida username y password. Se obtiene el token.
        final String token = userService.loginUser(credentialsDTO.getUsername(), credentialsDTO.getPassword());
        return JwtUserResponseDTO.builder()
                .username(credentialsDTO.getUsername())
                .token(token).build();
    }

    @Operation(summary = "Desloguea al usuario de la aplicación")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Ocurrió un error al realizar la operación"),
                            @ApiResponse(responseCode = "200", description = "El Usuario cerró sesión exitosamente")})
    @SecurityRequirements
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity logout() {
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.signOut(userDetails.getUsername());
        return new ResponseEntity(HttpStatus.OK);
    }


}
