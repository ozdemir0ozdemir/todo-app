package ozdemir0ozdemir.todoappbackend.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ozdemir0ozdemir.todoappbackend.dto.ErrorResponse;
import ozdemir0ozdemir.todoappbackend.exception.GlobalExceptionHandler;
import ozdemir0ozdemir.todoappbackend.model.Authority;
import ozdemir0ozdemir.todoappbackend.model.Member;
import ozdemir0ozdemir.todoappbackend.service.JwtService;
import ozdemir0ozdemir.todoappbackend.service.MemberService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private MemberService memberService;

    @Mock
    private GlobalExceptionHandler exceptionHandler;

    @InjectMocks
    private JwtRequestFilter jwtRequestFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain filterChain;


    @BeforeEach
    public void beforeEach() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = Mockito.mock(MockFilterChain.class);
    }

    @Test
    void it_should_not_filter_cause_of_authentication_request() throws ServletException, IOException {

        //Given
        request.setRequestURI("/authentication");

        // When
        this.jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void it_should_not_filter_cause_of_non_existence_of_authorization_header() throws Exception {

        //Given
        request.setRequestURI("/otherpath");

        //When
        this.jwtRequestFilter.doFilterInternal(request, response, filterChain);

        //Then
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void it_should_not_filter_cause_of_authorization_header_is_not_bearer() throws Exception {

        // Given
        request.setRequestURI("/otherpath");
        request.addHeader("Authorization", "OtherType mayBeValidToken");

        // When
        this.jwtRequestFilter.doFilterInternal(request, response, filterChain);

        // Then
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void it_should_throw_jwt_exception_extracting_username() throws ServletException, IOException {

        // Given
        final String baseToken = "invalidOrExpiredToken";
        final String exceptionMessage = "Exception Message";
        final HttpStatus errorStatus = HttpStatus.BAD_REQUEST;
        final LocalDateTime errorTimestamp = LocalDateTime.now();
        final String errorPath = "/errorPath";
        final String errorItemMessage = "ErrorItem Message";
        final String errorItemHelp = "ErrorItem Help";
        final String errorResponseJsonBody = "ErrorResponse JSON Body";

        final ErrorResponse.ErrorItem errorItem = ErrorResponse.ErrorItem.builder()
                .message(errorItemMessage)
                .help(errorItemHelp)
                .build();

        final ErrorResponse errorResponse = ErrorResponse.builder()
                .status(errorStatus)
                .timestamp(errorTimestamp)
                .path(errorPath)
                .errors(List.of(errorItem))
                .build();

        final JwtException ex = new JwtException(exceptionMessage);

        request.setRequestURI(errorPath);
        request.addHeader("Authorization", "Bearer " + baseToken);

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);

        // When
        Mockito.when(this.jwtService.extractUsername(tokenCaptor.capture()))
                .thenThrow(ex);
        Mockito.when(this.exceptionHandler.handleJwtException(ex, request))
                .thenReturn(ResponseEntity.status(errorStatus).body(errorResponse));
        Mockito.when(this.objectMapper.writeValueAsString(Mockito.any(ErrorResponse.class)))
                .thenReturn(errorResponseJsonBody);



        // Then
        this.jwtRequestFilter.doFilterInternal(request, response, filterChain);

        assertEquals(errorResponseJsonBody, response.getContentAsString(), "Response Jsons must be match");
        assertEquals(errorStatus.value(), response.getStatus(), "Response status must be match");
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType(), "Response types must be json and match");
        assertEquals(baseToken, tokenCaptor.getValue(), "Base tokens must be equal");

        Mockito.verify(this.jwtService, Mockito.times(1)).extractUsername(baseToken);
        Mockito.verify(this.exceptionHandler, Mockito.times(1)).handleJwtException(ex, request);

    }

    @Test
    void it_should_successfully_filter() throws ServletException, IOException {

        // Given
        final String baseToken = "validToken";
        final String requestUrl = "/requestUrl";
        final String username = "Username";
        final String password = "Password";
        final String authorityName = "Admin";
        final LocalDateTime memberTime = LocalDateTime.now();

        final Authority authority = Authority.builder()
                .id(1L)
                .authority(authorityName)
                .build();

        final Member member = Member.builder()
                .id(1L)
                .username(username)
                .isAccountExpired(false)
                .isAccountLocked(false)
                .isCredentialsExpired(false)
                .isEnabled(true)
                .password(password)
                .createdAt(memberTime)
                .updatedAt(memberTime)
                .authorities(List.of(authority))
                .build();


        request.setRequestURI(requestUrl);
        request.addHeader("Authorization", "Bearer " + baseToken);

        ArgumentCaptor<String> tokenCaptor = ArgumentCaptor.forClass(String.class);

        // When
        Mockito.when(this.jwtService.extractUsername(tokenCaptor.capture()))
                .thenReturn(username);

        Mockito.when(this.memberService.loadUserByUsername(username))
                .thenReturn(member);

        // Then
        this.jwtRequestFilter.doFilterInternal(request, response, filterChain);
        final var auth = SecurityContextHolder.getContext().getAuthentication();

        Mockito.verify(this.jwtService, Mockito.times(1))
                .extractUsername(Mockito.any());


        Mockito.verify(this.objectMapper, Mockito.times(0))
                        .writeValueAsString(Mockito.any());

        Mockito.verify(this.memberService, Mockito.times(1))
                .loadUserByUsername(username);

        assertEquals(baseToken, tokenCaptor.getValue(), "Base tokens must be equal");

        assertNotNull(auth);
        assertEquals(auth.getClass(), UsernamePasswordAuthenticationToken.class);
        assertSame(auth.getPrincipal(), member, "Principals must be same");
        assertTrue(auth.getAuthorities().contains(authority));




    }
}