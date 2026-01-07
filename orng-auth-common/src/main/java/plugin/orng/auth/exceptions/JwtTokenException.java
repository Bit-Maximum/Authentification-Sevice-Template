package plugin.orng.auth.exceptions;

import org.springframework.http.*;

public class JwtTokenException extends AuthAppException {

    public JwtTokenException() {
        super("Невалидный или просроченный токен", HttpStatus.UNAUTHORIZED);
    }

    public JwtTokenException(Throwable cause) {
        super("Невалидный или просроченный токен", HttpStatus.UNAUTHORIZED, cause);
    }

    protected JwtTokenException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    protected JwtTokenException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, httpStatus, cause);
    }

}
