package ozdemir0ozdemir.todoappbackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ozdemir0ozdemir.todoappbackend.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RestControllerAdvice
public class TaskExceptionHandler {

    /**
     * Jakarta @Valid
     * Field level validation global handler
     * @author Özdemir Özdemir
     * @param ex MethodArgumentNotValidException (@see MethodArgumentNotValidException)
     * @param request HttpServletRequest (@see HttpServletRequest)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Collection<ErrorResponse.ErrorItem> errorItems = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorItems.add(
                    ErrorResponse.ErrorItem.builder()
                            .message(error.getDefaultMessage())
                            // FIXME: Find proper way to include api address
                            .help("https://localhost:8080" + request.getRequestURI() + "/help")
                            .build()
            );
        });

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .errors(errorItems)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Spring @Validated on controller
     * Method argument level global handler
     * @author Özdemir Özdemir
     * @param ex ConstraintViolationException (@see ConstraintViolationException)
     * @param request HttpServletRequest (@see HttpServletRequest)
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ConstraintViolationException ex, HttpServletRequest request) {
        Collection<ErrorResponse.ErrorItem> errorItems = new ArrayList<>();

        ex.getConstraintViolations().forEach((violation) -> {
            errorItems.add(
                    ErrorResponse.ErrorItem.builder()
                            .message(violation.getMessage())
                            // FIXME: Find proper way to include api address
                            // FIXME: Omit path variable
                            .help("https://localhost:8080" + request.getRequestURI() + "/help")
                            .build()
            );
        });

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .errors(errorItems)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
}
