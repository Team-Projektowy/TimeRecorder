package com.timerecorder.fitlers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter implements javax.servlet.Filter {

    private final String jwtSecret;

    public JwtFilter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String header = httpServletRequest.getHeader("authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing bearer token");
            return;
        } else {
            try {
                String token = header.substring(7);
                Claims claims = Jwts
                        .parser()
                        .setSigningKey(jwtSecret)
                        .parseClaimsJws(token).getBody();
                servletRequest.setAttribute("claims", claims);
            } catch (final SignatureException e) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Incorrect bearer token");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
