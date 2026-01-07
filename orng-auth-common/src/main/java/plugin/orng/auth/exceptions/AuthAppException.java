package plugin.orng.auth.exceptions;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthAppException extends AuthenticationException {

    private HttpStatus httpStatus;

    public AuthAppException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AuthAppException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

}
