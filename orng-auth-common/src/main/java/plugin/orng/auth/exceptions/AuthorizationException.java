package plugin.orng.auth.exceptions;

import org.springframework.http.*;

public class AuthorizationException extends AuthAppException {

    public AuthorizationException() {
        super("Неправильный логин или пароль", HttpStatus.UNAUTHORIZED);
    }

    protected AuthorizationException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    protected AuthorizationException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, httpStatus, cause);
    }

}
