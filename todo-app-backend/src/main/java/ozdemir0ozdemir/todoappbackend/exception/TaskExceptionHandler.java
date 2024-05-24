package ozdemir0ozdemir.todoappbackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ozdemir0ozdemir.todoappbackend.dto.ErrorResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@RestControllerAdvice
public class TaskExceptionHandler {

    private final String mismatchArgumentString = "%s's value is invalid. Please provide proper value!";

    /**
     * Jakarta @Valid
     * Field level validation global handler
     *
     * @param ex      MethodArgumentNotValidException
     * @param request HttpServletRequest
     * @see MethodArgumentNotValidException
     * @see HttpServletRequest
     * @author Özdemir Özdemir
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {

        Collection<ErrorResponse.ErrorItem> errorItems = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            errorItems.add(
                    ErrorResponse.ErrorItem.builder()
                            .message(error.getDefaultMessage())
                            .help(request.getRequestURL() + "/help")
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
     *
     * @param ex      ConstraintViolationException
     * @param request HttpServletRequest
     * @see ConstraintViolationException
     * @see HttpServletRequest
     * @author Özdemir Özdemir
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ConstraintViolationException ex,
                                                          HttpServletRequest request) {

        Collection<ErrorResponse.ErrorItem> errorItems = new ArrayList<>();

        ex.getConstraintViolations().forEach((violation) -> {
            errorItems.add(
                    ErrorResponse.ErrorItem.builder()
                            .message(violation.getMessage())
                            // FIXME: Omit path variable
                            .help(request.getRequestURL() + "/help")
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
     * Exception handler for TaskNotFoundException
     * @param ex TaskNotFoundException is the exception
     * @param request HttpServletRequest is the main request
     * @see TaskNotFoundException
     * @see HttpServletRequest
     * @author Özdemir Özdemir
     * */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException ex,
                                                            HttpServletRequest request) {

        Collection<ErrorResponse.ErrorItem> errorItems = new ArrayList<>();

        errorItems.add(
                ErrorResponse.ErrorItem.builder()
                        .message(ex.getMessage())
                        // FIXME: Omit path variable
                        .help(request.getRequestURL() + "/help")
                        .build()
        );

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .errors(errorItems)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
    }

    /**
     * Exception handler for MethodArgumentTypeMismatchException
     * @param ex MethodArgumentTypeMismatchException is the exception
     * @param request HttpServletRequest is the main request
     * @see MethodArgumentTypeMismatchException
     * @see HttpServletRequest
     * @author Özdemir Özdemir
     * */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                          HttpServletRequest request) {

        Collection<ErrorResponse.ErrorItem> errorItems = new ArrayList<>();

        errorItems.add(
                ErrorResponse.ErrorItem.builder()
                        .message(String.format(mismatchArgumentString, ex.getName()))
                        // FIXME: Omit path variable
                        .help(request.getRequestURL() + "/help")
                        .build()
        );

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .errors(errorItems)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(response);
    }
}
