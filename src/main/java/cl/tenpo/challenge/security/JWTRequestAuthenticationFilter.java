package cl.tenpo.challenge.security;

import cl.tenpo.challenge.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ariel Miglio
 * @date 21/8/2021
 */
public class JWTRequestAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        Claims claims;
        String username;

        if (this.jwtTokenUtil.existJWTToken(request, response)) {
            token = this.jwtTokenUtil.getToken(request);
            claims = this.jwtTokenUtil.getAllClaimsFromToken(token);
            username = jwtTokenUtil.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.loadUserByUsername(username);

                if (this.jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    if (this.userService.hasValidToken(username)) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        SecurityContextHolder.clearContext();
                    }

                }
            }
        }
        filterChain.doFilter(request, response);

    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token;
//        Claims claims;
//        String username;
//
//        if (this.jwtTokenUtil.existJWTToken(request, response)) {
//            token = this.jwtTokenUtil.getToken(request);
//            claims = this.jwtTokenUtil.getAllClaimsFromToken(token);
//            username = jwtTokenUtil.getUsernameFromToken(token);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userService.loadUserByUsername(username);
//
//                if (this.jwtTokenUtil.validateToken(token, userDetails)) {
//                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
//                            userDetails.getAuthorities());
//                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    if (this.userService.hasValidToken(username)) {
//                        SecurityContextHolder.getContext().setAuthentication(auth);
//                    } else {
//                        SecurityContextHolder.clearContext();
//                    }
//
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//
//    }


}
