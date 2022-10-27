package com.lti.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class WebFluxSecurityConfig {

	@Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
		.csrf(csrf -> csrf.disable());
		
		return http.build();
    }
	
	@Bean
	CorsWebFilter corsFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		//corsConfig.setAllowCredentials(true);
		corsConfig.addAllowedOrigin("http://localhost:4200");
		corsConfig.addAllowedMethod("*");
		corsConfig.addAllowedHeader("*");
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return new CorsWebFilter(source);
	}
}
