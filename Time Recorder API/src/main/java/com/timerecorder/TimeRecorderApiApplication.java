package com.timerecorder;

import com.timerecorder.filters.IsAdminFilter;
import com.timerecorder.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    //Checks if user is logged int
    @Bean
    public FilterRegistrationBean<JwtFilter> userSignedInFilter() {
        FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(this.environment.getProperty("jwt.secret")));
        filterRegistrationBean.addUrlPatterns(
                "/time-records/*",
                "/users/*",
                "/tasks/*",
                "/change-password"
        );
        return filterRegistrationBean;
    }

    //Checks if user is logged int and is admin
    @Bean
    public FilterRegistrationBean<IsAdminFilter> userIsAdminFilter() {
        FilterRegistrationBean<IsAdminFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new IsAdminFilter(this.environment.getProperty("jwt.secret")));
        filterRegistrationBean.addUrlPatterns(
                "/register",
                "/users"
        );
        return filterRegistrationBean;
    }
}

