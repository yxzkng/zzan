package likelion.demo.auth.dto.response;

import likelion.demo.auth.dto.request.LoginRequest;
import likelion.demo.auth.entity.Member;
import likelion.demo.auth.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LoginResponse {
    private final String loginId;
    private final Role role;

    public static LoginResponse from(Member member){
        return LoginResponse.builder()
                .loginId(member.getLoginId())
                .role(member.getRole())
                .build();
    }
}
