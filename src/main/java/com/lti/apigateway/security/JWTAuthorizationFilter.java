package com.lti.apigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.lti.apigateway.utility.JWTUtil;

import reactor.core.publisher.Mono;

@Component
public class JWTAuthorizationFilter extends AbstractGatewayFilterFactory<JWTAuthorizationFilter.Config> {

	@Autowired
	JWTUtil jwtUtil;
	
	public JWTAuthorizationFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			System.out.println("First pre filter" + exchange.getRequest());
			
			if(!exchange.getRequest().getHeaders().containsKey("jwtToken")) {
				return returnErrorResponse(exchange);
			} else {
				final String jwtToken = exchange.getRequest().getHeaders().getOrEmpty("jwtToken").get(0);
				boolean isValidJWT = jwtUtil.validateToken(jwtToken);
				if(!isValidJWT) {
					return returnErrorResponse(exchange);
				} else {
					return chain.filter(exchange);
				}
			}
		   
			/* return chain.filter(exchange).then(Mono.fromRunnable(() -> {
				System.out.println("First post filter");
		    	})); */
	    	};
     }
   
	public static Mono<Void> returnErrorResponse(ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		return response.setComplete();
	}

    public static class Config {
    }
}
