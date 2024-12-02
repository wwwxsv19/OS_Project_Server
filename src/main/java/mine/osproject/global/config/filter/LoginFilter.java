package mine.osproject.global.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mine.osproject.domain.account.entity.account.CustomUserDetails;
import mine.osproject.domain.account.presentation.dto.AuthDto;
import mine.osproject.global.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    @PostConstruct
    public void init() {
        log.info("경로 /auth/login 에 대하여 LoginFilter 설정");
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("LoginFilter 진입");

        Map<String, String> loginRequest;

        try {
            loginRequest = objectMapper.readValue(request.getInputStream(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String userEmail = loginRequest.get("userEmail");
        String userPassword = loginRequest.get("userPassword");

        log.info("로그인 요쳥 UserEmail : {} / UserPassword : {}", userEmail, userPassword);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
        log.info("토큰이 생성되었습니다 : {}", authToken);

        return authManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("인증 성공");

        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String userEmail = customUserDetails.getUserEmail();
        String userName = customUserDetails.getUserName();
        String userImage = customUserDetails.getUserImage();
        String userRole = customUserDetails.getUserRole();

        log.info("로그인 JWT 토큰을 생성합니다 : {}", userEmail);
        String token = jwtUtil.createJwt(userEmail);
        log.info("로그인 JWT 토큰을 생성했습니다 : {}", token);

        response.addHeader("Authorization", "Bearer " + token);
        log.info("헤더에 토큰 추가");

        AuthDto.LoginResponse responseBody = AuthDto.LoginResponse.builder()
                .isLogged(true)
                .message("로그인에 성공했어요!")
                .userName(userName)
                .userImage(userImage)
                .build();

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));

        log.info("바디에 로그인 정보 추가");

        log.info("LoginFilter 통과 성공");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("인증 실패");

        AuthDto.LoginResponse responseBody = AuthDto.LoginResponse.builder()
                .isLogged(false)
                .message("로그인에 실패했어요...")
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));

        log.info("바디에 에러 메시지 추가");

        log.info("LoginFilter 통과 실패");
    }
}
