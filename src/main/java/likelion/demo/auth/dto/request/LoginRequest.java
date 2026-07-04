package likelion.demo.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    // TODO: loginId, password
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min=1, max=20, message="아이디는 1자 이상 20자 이하로 입력하세요.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min=1, max=20, message="비밀번호는 1자 이상 20자 이하로 입력하세요.")
    private String password;
}
