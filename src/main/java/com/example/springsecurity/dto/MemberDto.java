package com.example.springsecurity.dto;


import com.example.springsecurity.domain.member.MemberEntity;
import com.example.springsecurity.domain.member.Role;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class MemberDto {    // 프런트로부터 데이터를 전달받기 위해 사용하는 클래스.

    private int memberno;
    private String memberid;
    private String memberpassword;
    private String membernickname;
    private String memberemail;
    private String auth;


    public MemberEntity toEntity(){
        BCryptPasswordEncoder cryptPasswordEncoder =new BCryptPasswordEncoder();
            // 입력받은 password를 암호화 적용 후 빌드키 위해 암호화 객체 생성

        return MemberEntity.builder()
                .memberno(this.memberno)
                .memberid(this.memberid)
                .memberpassword(cryptPasswordEncoder.encode(this.memberpassword))   // 현 코드에서 비밀번호의 암호화 실시
                .membernickname(this.membernickname)
                .memberemail(this.memberemail)
                .auth(this.auth)    // 회원가입 경로 (Oauth, 홈페이지) 구분용    ->> 현재 미구현
                .role(Role.Member)  // 맴버권한 부여 하드코딩
                .build();
    }
}
