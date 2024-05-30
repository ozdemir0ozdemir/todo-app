package ozdemir0ozdemir.todoappbackend.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ozdemir0ozdemir.todoappbackend.configuration.JwtConfiguration;

import javax.crypto.SecretKey;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private JwtConfiguration jwtConfiguration;

    @InjectMocks
    private JwtService jwtService;

    // Common Objects
    private final String username = "test_username";
    private final String issuer = "TODO-APP";

    private Date currentDate;
    private Date expirationDate;
    private SecretKey secretKey;

    private String validToken;
    private String expiredToken;
    private final String malformedToken = "malformedTokenString";


    @BeforeEach
    public void beforeEach() {
        when(jwtConfiguration.getSecret()).thenReturn("0123456789abcdef0123456789abcdef0123456789abcdef");
        when(jwtConfiguration.getExpiration()).thenReturn(1000 * 60 * 60);

        this.currentDate = new Date(System.currentTimeMillis());
        this.expirationDate = new Date(System.currentTimeMillis() + jwtConfiguration.getExpiration());

        byte[] bytes = Decoders.BASE64.decode(jwtConfiguration.getSecret());
        this.secretKey = Keys.hmacShaKeyFor(bytes);

        this.validToken = Jwts.builder()
                .signWith(this.secretKey)
                .issuer(issuer)
                .issuedAt(this.currentDate)
                .subject(this.username)
                .expiration(this.expirationDate)
                .compact();

        this.expiredToken = Jwts.builder()
                .signWith(this.secretKey)
                .issuer(issuer)
                .issuedAt(this.currentDate)
                .subject(this.username)
                .expiration(new Date(this.currentDate.getTime() - jwtConfiguration.getExpiration()))
                .compact();
    }


    @Test
    void generateValidTokenWithUsernameTest() {

        // Given
        String token = jwtService.generateToken(username);

        // When
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        // Then
        assertEquals(username, claims.getPayload().getSubject(), "Usernames must be match");
        assertTrue(currentDate.before(claims.getPayload().getExpiration()), "Must provide non-expired token");
        assertEquals(claims.getHeader().getAlgorithm(), "HS256", "Tokens algorithms must be match");

    }

    @Test
    void isTokenExpiredTest() {
         // When
        Boolean result = jwtService.isTokenExpired(validToken);

        // Then
        assertFalse(result,"Token must be non-expired");
    }

    @Test
    void extractUsernameTest() {

        // When
        String result = jwtService.extractUsername(this.validToken);

        // Then
        assertEquals(username, result, "Usernames are not match");
    }

    @Test
    void extractExpirationTest() {

        // When
        Date result = jwtService.extractExpiration(validToken);

        // Then
        assertEquals(expirationDate.toString(), result.toString(), "Expiration dates are not match");
    }

    @Test
    void validateTokenTest() {

        // When
        Boolean result = jwtService.validateToken(validToken, username);

        // Then
        assertTrue(result,"Token must be valid");
    }

    @Test
    void extractAllClaimsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // Given
        Method method = this.jwtService.getClass().getDeclaredMethod("extractAllClaims", String.class);

        // When
        method.setAccessible(true);
        Claims validTokenClaims = (Claims) method.invoke(this.jwtService,validToken);

        // Then
        assertEquals(validTokenClaims.getIssuer(), this.issuer, "Issuers must be match");
        assertEquals(validTokenClaims.getIssuedAt().toString(), this.currentDate.toString(), "IssuedAt dates must be match");
        assertEquals(validTokenClaims.getSubject(), this.username, "Subjects must be match");
        assertEquals(validTokenClaims.getExpiration().toString(), this.expirationDate.toString(), "Expiration dates must be match");

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jwtService, expiredToken));
        assertEquals(exception.getCause().getClass(), ExpiredJwtException.class, "Must throw ExpiredJwtException");

        exception = assertThrows(InvocationTargetException.class, () -> method.invoke(this.jwtService, malformedToken));
        assertEquals(exception.getCause().getClass(), MalformedJwtException.class, "Must throw MalformedJwtException");

        method.setAccessible(false);
    }
}