package mine.osproject.domain.account.entity.account;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_tbl")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_image")
    private String userImage;

    @Column(name = "user_explain")
    private String userExplain;

    @Column(name = "user_email", updatable = false)
    private String userEmail;

    @Column(name = "user_password")
    private String userPassword;

    /* OneToMany Post 필드 추가 */
}
