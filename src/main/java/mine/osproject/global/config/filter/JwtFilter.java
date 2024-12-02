package mine.osproject.global.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mine.osproject.domain.account.entity.account.CustomUserDetails;
import mine.osproject.domain.account.entity.account.User;
import mine.osproject.global.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter 진입");

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("Jwt 토큰이 존재하지 않습니다.");
            filterChain.doFilter(request, response);
            return ;
        }

        String token = header.substring(7);
        log.info("토큰 추출 성공 : {}", token);

        if (jwtUtil.isExpired(token)) {
            throw new ServletException("만료된 토큰입니다.");
        }

        String userEmail = jwtUtil.getUserEmail(token);

        User user = User.builder()
                .userEmail(userEmail)
                .userPassword("")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        log.info("JwtFilter 통과");
        filterChain.doFilter(request, response);
    }
}
