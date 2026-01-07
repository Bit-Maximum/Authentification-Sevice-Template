package plugin.orng.auth.exceptions;

import org.springframework.http.*;

public class AuthenticationException extends AuthAppException {

    public AuthenticationException() {
        super("Неправильный логин или пароль", HttpStatus.UNAUTHORIZED);
    }

    public AuthenticationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    protected AuthenticationException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, httpStatus, cause);
    }

}
