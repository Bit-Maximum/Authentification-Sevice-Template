package plugin.orng.auth.handler;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.access.*;
import org.springframework.security.web.access.*;

public class OrngAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Обработчик исключения доступа, который устанавливает статус ответа 403.
     *
     * @param request HTTP-запрос
     * @param response HTTP-ответ
     * @param accessDeniedException Исключение, указывающее на запрещенный доступ
     * @throws IOException Если произошла ошибка ввода-вывода
     * @throws ServletException Если произошла ошибка выполнения сервлета
     */
    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setStatus(403);
    }

}
