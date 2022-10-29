package com.imagesManagement.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.imagesManagement.Exception.EmployeeAuthenticationSuccessHandler;


@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityAdapter extends WebSecurityConfigurerAdapter {
	
	 String[] staticResources  =  {
		        "/css/**",
		        "/user-images/**",
		        "/js/**",
		        "/denied",
		        "/image/display/{id}",
		    };
	 
    private final EmployeeAuthenticationSuccessHandler  loginSuccessHandler;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityAdapter(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder
    		,EmployeeAuthenticationSuccessHandler  loginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.loginSuccessHandler = loginSuccessHandler;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(staticResources).permitAll()
                .antMatchers("/user/*","/").permitAll()   
                .antMatchers("/admin").hasRole("ADMIN") 
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/denied")
                .and().formLogin().loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .logoutRequestMatcher
				(new AntPathRequestMatcher("/logout","GET"))
                .clearAuthentication(true)
		        .invalidateHttpSession(true)
                .logoutSuccessUrl("/login")
                .permitAll();

    }


}
