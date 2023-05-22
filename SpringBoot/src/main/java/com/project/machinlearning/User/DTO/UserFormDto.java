package com.project.machinlearning.User.DTO;
// 회원가입 화면으로부터 넘어오는 가입정보를 담을 Dto
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class UserFormDto {

    @NotEmpty(message = "닉네임은 필수 항목입니다.")
    @Length(max = 15, message = "최대 15자를 입력하세요.")
    private String nickName;

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    @Length(min = 6, max = 20, message = "최소 6자, 최대 20자를 입력하세요.")
    private String pw;

}
