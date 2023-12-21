package com.vukimphuc.paymentservice.utils;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(JwtUtils.class);
    private final String jwtSecret = "Spring";
    private final String jwtCookie = "SpringBoot";
    private final String jwtExpiration = "3525000";

    public String getJwtFromCookie(HttpServletRequest req){
        Cookie cookie = WebUtils.getCookie(req, jwtCookie);
        if(cookie != null){
            return cookie.getValue();
        }else{
            return null;
        }
    }

    public ResponseCookie cleanJwtCookie(){
        ResponseCookie cookie = ResponseCookie.from(jwtCookie,null).path("/").build();
        return cookie;
    }
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
    public String generateTokenFromUsername(String username){
        Instant instant = Instant.now().plusMillis(Long.parseLong(jwtExpiration));
        Date expirationDate = Date.from(instant);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
