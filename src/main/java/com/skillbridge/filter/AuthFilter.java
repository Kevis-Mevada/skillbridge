package com.skillbridge.filter;

import com.skillbridge.model.Role;
import com.skillbridge.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * AuthFilter ensures that protected routes (/student/*, /recruiter/*, /admin/*)
 * are strictly accessible only by authenticated users holding the required role.
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = {"/*"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String path = uri.substring(contextPath.length());

        // Allow static assets and public endpoints
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("currentUser") : null;

        // Check if user is logged in
        if (currentUser == null) {
            // Unauthenticated user attempting to access protected resource
            if (isProtectedPath(path)) {
                resp.sendRedirect(contextPath + "/login");
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // Set security headers to prevent caching sensitive user pages
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setDateHeader("Expires", 0);

        // Role-based Access Control
        if (path.startsWith("/student/")) {
            if (currentUser.getRole() != Role.STUDENT) {
                redirectByRole(currentUser, resp, contextPath);
                return;
            }
        } else if (path.startsWith("/recruiter/")) {
            if (currentUser.getRole() != Role.RECRUITER) {
                redirectByRole(currentUser, resp, contextPath);
                return;
            }
        } else if (path.startsWith("/admin/")) {
            if (currentUser.getRole() != Role.ADMIN) {
                redirectByRole(currentUser, resp, contextPath);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.equals("/") ||
               path.equals("/index.jsp") ||
               path.startsWith("/login") ||
               path.startsWith("/register") ||
               path.startsWith("/logout") ||
               path.startsWith("/jobs") ||
               path.startsWith("/404") ||
               path.startsWith("/500") ||
               path.startsWith("/error") ||
               path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/");
    }

    private boolean isProtectedPath(String path) {
        return path.startsWith("/student/") ||
               path.startsWith("/recruiter/") ||
               path.startsWith("/admin/");
    }

    private void redirectByRole(User user, HttpServletResponse resp, String contextPath) throws IOException {
        switch (user.getRole()) {
            case STUDENT:
                resp.sendRedirect(contextPath + "/student/dashboard");
                break;
            case RECRUITER:
                resp.sendRedirect(contextPath + "/recruiter/dashboard");
                break;
            case ADMIN:
                resp.sendRedirect(contextPath + "/admin/dashboard");
                break;
            default:
                resp.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {}
}
