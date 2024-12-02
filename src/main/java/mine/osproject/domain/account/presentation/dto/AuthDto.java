package mine.osproject.domain.account.presentation.dto;

import lombok.Builder;
import lombok.Getter;

public class AuthDto {
    @Builder
    @Getter
    public static class LoginResponse {
        private boolean isLogged;
        private String message;
        private String userName;
        private String userImage;
    }
}
