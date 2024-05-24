package ozdemir0ozdemir.todoappbackend.exception;

public class TaskNotFoundException extends RuntimeException{

    public TaskNotFoundException(String message){
        super(message);
    }
}
