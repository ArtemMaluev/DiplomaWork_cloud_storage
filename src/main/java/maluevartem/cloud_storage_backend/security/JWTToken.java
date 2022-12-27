package maluevartem.cloud_storage_backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JWTToken {

    private final SecretKey secret;
    private final int tokenLifetime;
    private final List<String> listTokens = new ArrayList<>();
    private UserEntity userEntity;

    public JWTToken(@Value("${jwt.secret}") String secret, @Value("${TOKEN_LIFETIME}") int tokenLifetime) {
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.tokenLifetime = tokenLifetime;
    }

    public UserEntity getAuthenticatedUser() {
        return userEntity;
    }

    public String generateToken(@NonNull UserEntity userEntity) throws IllegalArgumentException {
        this.userEntity = userEntity;
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(tokenLifetime)
                .atZone(ZoneId.systemDefault()).toInstant());

        String token = Jwts.builder()
                .setId(String.valueOf(userEntity.getId()))
                .setSubject(userEntity.getLogin())
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp)
                .signWith(secret)
                .compact();
        listTokens.add(token);
        return token;
    }

    public boolean validateAccessToken(@NonNull String token) {
        for (String t : listTokens) {
            if (t.equals(token)) {
                return false;
            }
        }
        return validateToken(token, secret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            //log.error("Token expired", expEx);
        } catch (UnsupportedJwtException unsEx) {
            //log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            //log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            //log.error("Invalid signature", sEx);
        } catch (Exception e) {
            //log.error("invalid token", e);
        }
        return false;
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, secret);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void removeToken(String token) {
        listTokens.remove(token);
    }
}
