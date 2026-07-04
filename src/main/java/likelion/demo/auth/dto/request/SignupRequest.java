package likelion.demo.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import likelion.demo.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    // TODO: loginId, password, phoneNumber, role

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min=1, max=20, message="아이디는 1자 이상 20자 이하로 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min=1, max=20, message="비밀번호는 1자 이상 20자 이하로 입력하세요.")
    private String password;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(min=11, max=11, message="11글자 전화번호를 입력하세요.")
    private String phoneNumber;

    @NotNull(message = "학생과 사장님 중 하나를 선택하세요.")
    private Role role;
}
