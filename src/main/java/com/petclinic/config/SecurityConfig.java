package com.petclinic.config;

import com.petclinic.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/images/**")
                .permitAll()
                .antMatchers("/login")
                .permitAll();

        http.authorizeRequests().antMatchers("/examination/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/examination/edit/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/pets/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/pets/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/owner/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/owner/update/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/user/delete/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();

        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

}
