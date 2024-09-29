package io.github.seujorgenochurras.p2pApi.api.security.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.seujorgenochurras.p2pApi.api.security.detail.UserDetailsImplService;
import io.github.seujorgenochurras.p2pApi.domain.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsImplService userDetailsImplService;

    @Autowired
    private JwtService jwtService;


    public JwtFilter() {
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestToken = getToken(request);
        if (requestToken != null) {
            DecodedJWT decodedRequestToken = jwtService.decodeJwt(requestToken);
            String username = decodedRequestToken.getSubject();

            UserDetails requestClient = userDetailsImplService.loadUserByUsername(username);

            boolean validJwt = jwtService.validateJwt(requestToken, requestClient);
            if (validJwt) {
                var userNamePasswordAuthToken = new UsernamePasswordAuthenticationToken(
                    requestClient,
                    null,
                    requestClient.getAuthorities());

                userNamePasswordAuthToken.setDetails(new WebAuthenticationDetails(request));

                SecurityContextHolder.getContext().setAuthentication(userNamePasswordAuthToken);
            }
        }
        filterChain.doFilter(request, response);

    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.contains("Bearer eyJ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
