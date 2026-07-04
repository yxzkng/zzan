package likelion.demo.auth.dto.response;

import likelion.demo.auth.entity.Member;
import likelion.demo.auth.entity.Role;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;

@Getter
@Builder
public class SignupResponse {
    private final String loginId;
    private final Role role;

    public static SignupResponse from(Member member){
        return SignupResponse.builder()
                .loginId(member.getLoginId())
                .role(member.getRole())
                .build();
    }
    
}
