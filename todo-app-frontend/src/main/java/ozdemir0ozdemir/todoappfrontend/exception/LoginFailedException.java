package ozdemir0ozdemir.todoappfrontend.exception;


import ozdemir0ozdemir.todoappfrontend.dto.ErrorResponse;

public class LoginFailedException extends RuntimeException{

    private final ErrorResponse errorResponse;

    public LoginFailedException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse(){
        return this.errorResponse;
    }
}
