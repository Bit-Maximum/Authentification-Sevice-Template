package plugin.orng.auth;

import jakarta.servlet.http.*;

public class JwtTokenExtractor {

    public static final String BEARER = "Bearer ";

    public static final String AUTH_HEADER = "Authorization";

    private JwtTokenExtractor() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Извлекает Bearer токен из HttpServletRequest.
     *
     * @param request Токен для извлечения токена
     *
     * @return Bearer токен, извлечённый из HttpServletRequest
     */
    public static String extractToken(HttpServletRequest request) {
        String header = request.getHeader(AUTH_HEADER);

        if (header == null || !header.startsWith(BEARER)) {
            throw new RuntimeException("Не найден Bearer токен");
        }

        return header.substring(BEARER.length());
    }

    /**
     * Извлекает Bearer токен из HttpServletRequest.
     *
     * @param header Содержание заголовка для извлечения токена
     *
     * @return Bearer токен, извлечённый из HttpServletRequest
     */
    public static String extractToken(String header) {

        if (header == null || !header.startsWith(BEARER)) {
            throw new RuntimeException("Не найден Bearer токен");
        }

        return header.substring(BEARER.length());
    }

}
