package com.example.config;

import com.example.Filter.JWTLoginFilter;
import com.example.Filter.JwtAuthenticationFilter;
import com.example.service.MyUserRepositoryService;
import com.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class MyWebSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MyUserRepositoryService myUserRepositoryService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//                http.formLogin()
//                .loginPage("/login.html")           // 设置登录页面
//                .and()
//                .cors().and()
//                .csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/login","/users/signup","/error").permitAll()
//                .antMatchers("/login.html","/js/**","/index.html","/index2.html").permitAll()// 设置所有人都可以访问登录页面
//                .anyRequest().authenticated()
//                .and()
//                .addFilter(new JWTLoginFilter(authenticationManager()))
//                .addFilter(new JwtAuthenticationFilter(authenticationManager()));

        http
//                .formLogin()//如果需要页面跳转放开fromlogin,设置登录页
//                .loginPage("/login.html")
//                .and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/signup","/login").permitAll()
                .antMatchers("/login.html","/js/**","/index.html","/index2.html").permitAll()// 设置所有人都可以访问登录页面
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager() , myUserRepositoryService))
                .addFilter(new JwtAuthenticationFilter(authenticationManager() , myUserRepositoryService));

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
