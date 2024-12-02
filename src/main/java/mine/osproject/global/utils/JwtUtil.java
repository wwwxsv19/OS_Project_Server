package mine.osproject.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private String exprTime;

    public String createJwt(String userEmail) {
        log.info("Create JWT User : {}", userEmail);

        Claims claims = Jwts.claims();
        claims.put("userEmail", userEmail);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exprTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("userEmail", String.class);
        } catch (Exception e) {
            log.error("토큰으로부터의 사용자 이메일 추출에 실패했습니다.");
            return null;
        }
    }

    // 토큰이 만료되었다면 true, 아직 유효하다면 false
    public boolean isExpired(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("만료된 토큰입니다. {}", token);
            return true;
        }
    }
}
