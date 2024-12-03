package io.github.seujorgenochurras.p2pApi.domain.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.seujorgenochurras.p2pApi.api.controller.client.FindClientService;
import io.github.seujorgenochurras.p2pApi.domain.model.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtService {

    private static final Dotenv env = Dotenv.configure().load();
    private static final Algorithm SIGN_ALGORITHM = Algorithm.HMAC256(env.get("JWT_SECRET"));
    private static final String ISSUER = "p2pcraft-api";

    @Autowired
    private FindClientService findClientService;

    public String createJwt(UserDetails userDetails) {
        return createJwt(userDetails, expirationDate(), currentTime());
    }

    public String createJwt(UserDetails userDetails, Instant expiresAt, Instant issuedAt) {
        return JWT.create()
            .withIssuer(ISSUER)
            .withIssuedAt(issuedAt)
            .withExpiresAt(expiresAt)
            .withSubject(userDetails.getUsername())
            .sign(SIGN_ALGORITHM);
    }

    public DecodedJWT decodeJwt(String jwt) throws JWTDecodeException {
        return JWT.decode(jwt);
    }

    public boolean validateJwt(String jwt, UserDetails userDetails) throws JWTDecodeException {
        DecodedJWT decodedJWT = decodeJwt(jwt);
        String requestSignature = decodedJWT.getSignature();
        DecodedJWT validJwt = decodeJwt(createJwt(userDetails, decodedJWT.getExpiresAtAsInstant(), decodedJWT.getIssuedAtAsInstant()));
        Instant expiresAt = decodedJWT.getExpiresAtAsInstant();
        Client client = findClientService.findById(userDetails.getUsername());
        return requestSignature.equals(validJwt.getSignature()) && expiresAt.isAfter(currentTime()) && client != null;
    }

    private Instant currentTime() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
    }

    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(6).toInstant();
    }
}
