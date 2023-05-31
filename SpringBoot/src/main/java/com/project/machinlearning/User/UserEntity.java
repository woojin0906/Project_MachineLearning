package com.project.machinlearning.User;

import com.project.machinlearning.Diary.DiaryEntity;
import com.project.machinlearning.Recommend.RecommendEntity;
import com.project.machinlearning.User.DTO.BanRequestDTO;
import com.project.machinlearning.User.DTO.UserFormDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
@Setter
@Entity
@ToString(exclude = {"diaries", "recommends"})
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name="pw")
    private String pw;

    @Column(name="nickName", unique = true)
    private String nickName;

    @Column(name="count" , columnDefinition = "int default 0")
    private int count;

    @Enumerated(EnumType.STRING) // 추가를 안하면 열거형은 숫자로 들어감
    private Role role;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<DiaryEntity> diaries;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<RecommendEntity> recommends;

    //사용자 만들기 -> 받아온 DTO를 entity로 변경
    public static UserEntity createUser(UserFormDto userFormDto, PasswordEncoder passwordEncoder) {
        UserEntity user = new UserEntity();
        user.setPw(userFormDto.getPw());
        user.setNickName(userFormDto.getNickName());
        user.setRole(Role.USER);  //기본으로 다 USER로 가입

        String password = passwordEncoder.encode(userFormDto.getPw());  //일단 비밀번호를 플레인 텍스트로 받아온 후 encode를 이용해서 암호화
        user.setPw(password);  //암호화된 패스워드 삽입

        //만든 user 리턴
        return user;
    }

    public void updateItem() {
        this.role = Role.BAN;
    }
}
