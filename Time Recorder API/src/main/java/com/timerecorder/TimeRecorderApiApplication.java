package com.timerecorder;

import com.timerecorder.filters.IsAdminFilter;
import com.timerecorder.filters.JwtFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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

    //Checks if user is logged int
    @Bean
    public FilterRegistrationBean<JwtFilter> userSignedInFilter() {
        FilterRegistrationBean<JwtFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(this.environment.getProperty("jwt.secret")));
        filterRegistrationBean.addUrlPatterns(
                "/time-records/*",
                "/users/*"
        );
        return filterRegistrationBean;
    }

    //Checks if user is logged int and is admin
    @Bean
    public FilterRegistrationBean<IsAdminFilter> userIsAdminFilter() {
        FilterRegistrationBean<IsAdminFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new IsAdminFilter(this.environment.getProperty("jwt.secret")));
        filterRegistrationBean.addUrlPatterns(
                "/tasks/*",
                "/register"
        );
        return filterRegistrationBean;
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:4000")
//                        .allowedMethods("*");
//            }
//        };
//    }
}

