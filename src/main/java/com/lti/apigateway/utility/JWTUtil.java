package com.lti.apigateway.utility;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTUtil {

	@Value("${jwt.secretkey}")
	private String jwtSecret;

	@Value("${jwt.token.validity}")
	private long tokenValidity;	//5 mins
	
	public Claims getClaims(final String token) {
		try {
			Claims body = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
			return body;
		} catch (Exception e) {
			System.out.println(e.getMessage() + " => " + e);
		}
		return null;
	}
	
	public boolean validateToken(final String token) {
		try {
			System.out.println("validate tokennnnnnnn ");
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
			
		} catch (SignatureException ex) {
			throw new SignatureException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			throw new MalformedJwtException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new JwtException("JWT Token Expired");
		} catch (UnsupportedJwtException ex) {
			throw new UnsupportedJwtException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("JWT claims string is empty.");
		} catch (JwtException ex) {
            throw new JwtException("JWT exception");
        }
	}
}
