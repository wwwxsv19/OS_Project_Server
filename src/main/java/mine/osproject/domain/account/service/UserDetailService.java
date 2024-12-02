package mine.osproject.domain.account.service;

import lombok.RequiredArgsConstructor;
import mine.osproject.domain.account.entity.account.CustomUserDetails;
import mine.osproject.domain.account.entity.account.User;
import mine.osproject.domain.account.entity.account.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail);

        if (user == null) {
            throw new UsernameNotFoundException("해당 사용자의 UserDetails 를 찾을 수 없습니다 : " + userEmail);
        }

        return new CustomUserDetails(user);
    }
}
