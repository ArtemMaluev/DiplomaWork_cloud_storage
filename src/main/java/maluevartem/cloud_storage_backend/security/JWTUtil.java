package maluevartem.cloud_storage_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import maluevartem.cloud_storage_backend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTUtil {

    private UserEntity user;
    @Value("${jwt.secret}")
    private String secret;

    @Value("${TOKEN_LIFETIME}")
    private int tokenLifetime;

//    private UserEntity getUser() {
//        return user;
//    }

    public String generateToken(UserEntity userEntity) throws IllegalArgumentException, JWTCreationException {
        user = userEntity;
        Date now = new Date();
        Date exp = Date.from(LocalDateTime.now().plusMinutes(tokenLifetime)
                .atZone(ZoneId.systemDefault()).toInstant());

        return JWT.create()
                .withJWTId(String.valueOf(userEntity.getId()))
                .withSubject(userEntity.getLogin())
                .withIssuedAt(now)
                .withNotBefore(now)
                .withExpiresAt(exp)
                .sign(Algorithm.HMAC256(secret));
    }


//    public String validateToken(String token) {
//
//        return null;
//    }

}
