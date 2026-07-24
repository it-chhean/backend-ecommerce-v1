package co.taskflow.ecommerce.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    Key getSigningKey();

    String generateToken(String subject);

    String generateToken(Map<String , Object> claims , String subject);

    String extractEmail(String token);

    <T> T extracCliam(String token, Function<Claims, T> claimsTFunction);

    boolean isTokenExpiration(String token);

    boolean isTokenValid(String token , UserDetails userDetails);

    long getExpiration();
}