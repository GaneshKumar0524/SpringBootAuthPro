package com.project.SpringBootAuthPro.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.project.SpringBootAuthPro.service.CustomUserDetailsService;
import com.project.SpringBootAuthPro.utility.JwtFilter;

import java.beans.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtFilter jwtFilter;
	@Autowired
	public SecurityConfig(JwtFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
	}


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.maximumSessions(1)
						.maxSessionsPreventsLogin(true))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/auth/**" ,
                                "/",
								"/actuator/**" , 
								"/swagger-ui/**" ,
								"/v3/api-docs/**" , 
								"/swagger-ui.html",
                                "/login/oauth2/**").permitAll()
						.anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/private", true))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // ✅ This is critical!
				.build();
	}
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder()) // Ensure password encoding
				.and()
				.build();
	}
}