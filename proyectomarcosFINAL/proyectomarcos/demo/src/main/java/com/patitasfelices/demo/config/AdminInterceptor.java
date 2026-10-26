package com.patitasfelices.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

/** Solo deja entrar a /admin/** a quien inició sesión con rol Administrador. */
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        Object rol = (session != null) ? session.getAttribute("rol") : null;

        if ("Administrador".equals(rol)) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/login?acceso");
        return false;
    }
}
