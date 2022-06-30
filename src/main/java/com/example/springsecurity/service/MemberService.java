package com.example.springsecurity.service;

import com.example.springsecurity.domain.member.MemberEntity;
import com.example.springsecurity.domain.member.MemberRepository;
import com.example.springsecurity.dto.LoginDto;
import com.example.springsecurity.dto.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service // 현 클래스를 서비스 클래스로 지정
public class MemberService implements UserDetailsService {
    // UserDetailsService
    // 1. 로그인 서비를 제공하는 인터페이스
    // 2. 패스워드 검증은 시큐리티에서 처리
    // 3. 아이디 검증만을 처리

    @Autowired MemberRepository memberRepository;


    // 회원가입 처리 메서드
    @Transactional  // SQL 트랜잭션 처리를 위한 어노테이션
    public boolean signup(MemberDto memberDto){
        MemberEntity memberEntity = memberDto.toEntity();   // toEntity()에 암호화 메서드 있음.
        memberRepository.save(memberEntity);    // JpaRepository를 통해 sql에 저장
        return true;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MemberEntity> optionalMember = memberRepository.findByMemberid(username);
            // username으로 SQL에서 memberid 검색

        MemberEntity memberEntity = optionalMember.orElse(null);
            // isPresent()와 같음. 값을 대입하거나 return null;
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(memberEntity.getRole().getKEY()));
            // 찾은 회원의 권한값을 해시셋에 추가

        return new LoginDto(memberEntity, authorities); // 로그인용 Dto를 생성 후 반환
    }
}
