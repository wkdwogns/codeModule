package com.nexon.common.security;

import com.nexon.membership.config.ConfigMembership;
import com.nexon.membership.customHandler.CustomAccessDeniedHandler;
import com.nexon.membership.customHandler.CustomLoginFailureHandler;
import com.nexon.membership.customHandler.CustomLoginSuccessHandler;
import com.nexon.membership.customHandler.CustomLogoutHandler;
import com.nexon.membership.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MembershipService logInService;

    @Autowired
    ConfigMembership configMembership;

    @Autowired
    CustomLogoutHandler customLogoutHandler;

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationFailureHandler loginFailuredHandler(){
        return new CustomLoginFailureHandler();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() { return new CustomLoginSuccessHandler(); }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        if(configMembership.isUseMemberShip()) {
            web.ignoring().antMatchers("/css/**", "/script/**", "/images/**", "/fonts/**", "lib/**", "/static/**", "/resources/**");
        }
        else {
            web.ignoring().antMatchers("/**");
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(configMembership.isUseMemberShip()) {
            http
                    .csrf().disable()
                    .authorizeRequests().and().cors()
                    .and()
                    .formLogin()
                    .usernameParameter("userId")
                    .passwordParameter("userPwd")
                    .loginProcessingUrl("/api/login")
                    .successHandler(successHandler())
                    .failureHandler(loginFailuredHandler())
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(customLogoutHandler)
                    .invalidateHttpSession(true)
                    .permitAll()
                    .and()
                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                    .and()
                    .rememberMe().key("uniqueAndSecret")
                    .rememberMeParameter("rememberMe")
            ;
        }
        else
        {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**").permitAll()
            ;
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        if(configMembership.isUseMemberShip()) {
            auth.userDetailsService(logInService)
                    .passwordEncoder(logInService.passwordEncoder());
        }
    }
}
