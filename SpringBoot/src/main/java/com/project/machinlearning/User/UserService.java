package com.project.machinlearning.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class UserService implements UserDetailsService  {

    private final UserRepository userRepository;

    public UserEntity saveUser(UserEntity userEntity) {
        // 중복확인
        validateDuplicate(userEntity);
        // 중복확인 후 회원 저장
        return userRepository.save(userEntity);
    }

    private void validateDuplicate(UserEntity userEntity) {
        // 닉네임으로 찾았을 때 값이 있는지 없는지 판단
        UserEntity findUser = userRepository.findByNickName(userEntity.getNickName());
        // DB에서 닉네임 검색이 되면 이미 등록되어있는 회원이라는 알림 표시
        if(findUser != null) {
            throw new IllegalStateException("이미 등록된 사용자 입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String nickName) throws UsernameNotFoundException {
        // DB에 있는 nickName을 찾아서 가져옴
        UserEntity userEntity = userRepository.findByNickName(nickName);

        // 멤버가 없는 경우 (기존에 사용자가 없는 경우)
        if(userEntity == null) {
            //이메일 정보를 추가해서 exception을 날린다. 이거 근데 로그인이 잘못됐다는 예외가 발생한다
            throw new UsernameNotFoundException("해당 사용자가 없습니다." + nickName);
        }

        log.info("=================> loadUserByUsername : " + userEntity);

        return User.builder()
                .username(userEntity.getNickName())//이메일, 아이디 등 어떤 로직으로 로그인을 하던 그 로그인을 할 필드를 넣어준다.
                .password(userEntity.getPw()) //암호화 되어서 들어가야 함.
                .roles(userEntity.getRole().toString())//역할을 string으로 넣어줘야 한다. -> enum 안됨
                .build();
    }

}
