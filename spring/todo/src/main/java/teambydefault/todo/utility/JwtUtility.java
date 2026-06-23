package teambydefault.todo.utility;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import teambydefault.todo.entity.User;

@Component
public class JwtUtility {
     private final String SECRET = "this-works-for-dev-use-environemnt-in-prod";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getId().toString())// Who this token is for
                .claim("email", user.getEmail())// extra information about the user we want to access
                .issuedAt(new Date(System.currentTimeMillis())) // when the token was created
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // when the token expires (24hr)
                .signWith(key) // signing the jwt with our key for encryption
                .compact(); // encoding it all into a String
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser()
                .verifyWith(key) // we tell the parser what key to use to decrypt the jwt
                .build()
                .parseSignedClaims(token); // the parser attempts to read the jwt: if they jwt is valid no exceptions 
                                           // are thrown, the jwt is valid, and we return true 
            return true;
        } catch (JwtException | IllegalArgumentException e){
            /*
                We catch JwtException to handle any problems with the JWT itself, such as being expired,
                malformed, or tampered with.

                We catch IllegalArgumentException to handle issues with the token not being present or empty,
                any any other issue that would cause the parser to be unable to interact with it
            */
            return false;
        }
    }

}
