package ozdemir0ozdemir.todoappbackend.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.todoappbackend.configuration.JwtConfiguration;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtConfiguration jwtConfiguration;

    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    public Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public Boolean validateToken(String token, String username) {

        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfiguration.getSecret()));

        return Jwts.parser().
                verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    private String createToken(Map<String, Object> claims, String username) {

        SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfiguration.getSecret()));

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtConfiguration.getExpiration()))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

}
