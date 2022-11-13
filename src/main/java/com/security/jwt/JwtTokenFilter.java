package com.security.jwt;

import com.controllers.RestAuthenticationEntryPoint;
import com.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class JwtTokenFilter extends GenericFilterBean {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    private List<String> publicUrls = Arrays.asList(
            "/api/token",
            "/api/courses",
            "/api/course/course",
            "/api/course_parts/course_part",
            "/api/timezone",
            "/api/avatar/get",
            "/api/avatar/get_for_user",
            "/api/course_sections",
            "/api/timezone",
            "/api/users/exists_by_login",
            "/api/users/exists_by_email",
            "/api/users/add",
            "/api/users/accept_email",
            "/api/users/get_user",
            "/api/files/certificate",
            "/api/users/send_reset_password_link",
            "/api/users/reset_password",
            "/api/users/is_reset_password_link_expired"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain)
            throws IOException, ServletException {
        String requestUr = ((HttpServletRequest) req).getRequestURL().toString();

        for(String publicUrl: this.publicUrls){
            if(requestUr.contains(publicUrl) && !requestUr.contains("/api/token/logout")) {
                filterChain.doFilter(req, res);
                return;
            }
        }

        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);

                if (auth != null) {
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(req, res);
        } catch (JwtAuthenticationException|UsernameNotFoundException ex){
            SecurityContextHolder.clearContext();
            new RestAuthenticationEntryPoint().commence((HttpServletRequest)req, (HttpServletResponse) res, ex);
        }
    }

}
