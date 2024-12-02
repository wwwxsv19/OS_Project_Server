package mine.osproject.domain.account.entity.account.repository;

import mine.osproject.domain.account.entity.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserEmail(String userEmail);
}
