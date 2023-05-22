package com.project.machinlearning.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 문자로 된 닉네임을 넣어서 검색
    UserEntity findByNickName(String nickName);  // nickName 받아오기

}
