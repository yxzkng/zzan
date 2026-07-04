package likelion.demo.auth.service;

import likelion.demo.auth.dto.response.LoginResponse;
import likelion.demo.auth.dto.response.SignupResponse;
import likelion.demo.auth.entity.Member;
import likelion.demo.auth.entity.Role;
import likelion.demo.auth.repository.MemberRepository;
import likelion.demo.global.exception.DuplicateLoginIdException;
import likelion.demo.global.exception.LoginFailedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    // TODO: 회원가입, 로그인, 로그아웃 비즈니스 로직 구현

    private final MemberRepository memberRepository;

    @Transactional
    public SignupResponse addMember(String loginId, String password, String phoneNumber, Role role){
        if (memberRepository.existsByLoginId(loginId)) {
            throw new DuplicateLoginIdException("이미 사용 중인 아이디입니다.");
        }

        Member member = new Member(loginId, password, phoneNumber, role);
        memberRepository.save(member);

        return SignupResponse.from(member);
    }

    @Transactional
    public LoginResponse login(String loginId, String password){
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if(!member.getPassword().equals(password)){
            throw new LoginFailedException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        return LoginResponse.from(member);
    }
}
