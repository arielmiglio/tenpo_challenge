package cl.tenpo.challenge.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Ariel Miglio
 * @date 21/8/2021
 */

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.token.validity}")
    private long jwtTokenValidity;

    @Value("${jwt.secret}")
    private String secret;

    private final String HEADER = "Authorization";
    private final String PREFIX = "Bearer ";

    /**
     * Obtiene el username desde los Claims del token
     * @param token
     * @return username
     */
    public String getUsernameFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retorna la fecha de expiración del token
     * @param token
     * @return Date - expiration
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Obtiene el mapa de Claims del token desde el cual se ejecuta una Function para obtener el atributo que haga falta
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene el Claims del token recibido
     * @param token
     * @return Claim
     */
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * Chequea si el token expiró
     * @param token
     * @return true si está expirado - false en caso contrario
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Genera un token para los datos de usuario recibidos
     * @param userDetails
     * @return Nuevo Token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Generación del token con el agregado de los Claims: subjet, expiration, issuer, id
     * Utiliza el algoritmo HS512 para firmar el JWT
     * @param claims
     * @param subject
     * @return String con el token creado
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * Valida el token para un usuario
     * @param token
     * @param userDetails
     * @return true cuando es válido, false en caso contrario
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrae y retorna el token desde el HEADER
     * @param request
     * @return
     */
    public String getToken(HttpServletRequest request) {
        return request.getHeader(HEADER).replace(PREFIX, "");
    }

    /**
     * Chequea que exista el token
     * @param request
     * @param res
     * @return true si existe token, false caso contrario
     */
    public boolean existJWTToken(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
            return false;
        return true;
    }
}
