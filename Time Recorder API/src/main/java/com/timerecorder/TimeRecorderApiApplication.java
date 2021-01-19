package com.timerecorder;

import com.timerecorder.fitlers.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

@SpringBootApplication
public class TimeRecorderApiApplication {

    private final Environment environment;
    public TimeRecorderApiApplication(Environment env) {
        this.environment = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(TimeRecorderApiApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterRegistrationBean<JwtFilter> filterRegistrationBean() {
        FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(this.environment.getProperty("jwt.secret")));
        filterRegistrationBean.setUrlPatterns(Collections.singleton("/home"));
        return filterRegistrationBean;
    }
}
