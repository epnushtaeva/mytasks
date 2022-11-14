package com.config;

import com.security.JwtUserDetailsService;
import com.security.jwt.JwtConfigurer;
import com.security.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtConfigurer configurer;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    public PasswordEncoder passwordEncoder(){
        PasswordEncoder encoder = new BCryptPasswordEncoder(12);
        return encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/token**").permitAll()
                .antMatchers("/api/token/**").permitAll()
                .antMatchers("/api/register**").permitAll()
                .antMatchers("/api/register/*").permitAll()
                .antMatchers("/api/board**").authenticated()
                .antMatchers("/api/board/**").authenticated()
                .antMatchers("/api/status**").authenticated()
                .antMatchers("/api/status/**").authenticated()
                .antMatchers("/api/profile_inf**").authenticated()
                .antMatchers("/api/profile_inf/**").authenticated()
                .antMatchers("/api/users**").authenticated()
                .antMatchers("/api/users/**").authenticated()
                .antMatchers("/api/task**").authenticated()
                .antMatchers("/api/task/**").authenticated()
//                .antMatchers("/api/theme**").permitAll()
//                .antMatchers("/api/theme/**").permitAll()
                .and()
                .apply(configurer);
    }
}
