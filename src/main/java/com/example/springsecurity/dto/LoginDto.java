package com.example.springsecurity.dto;

import com.example.springsecurity.domain.member.MemberEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class LoginDto implements UserDetails {
    // UserDetails : 로그인시 실질적으로 사용자 인증을 구현하는 인터페이스.

    private String memberid;
    private String memberpassword;
    private final Set<GrantedAuthority> authorities;    // Role에 있는 권한을 담을 필드

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
        // UserDetails로부터 로그인한 사용자의 권한을 전달받는다.

    @Override
    public String getPassword() {
        return this.memberpassword;
    }
        // 상동

    @Override
    public String getUsername() {
        return this.memberid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
        // 만료된 계쩡인이 확인

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
        // 잠긴 계정인지 확인

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
        // 인증값이 만료되었는지 확인
    @Override
    public boolean isEnabled() {
        return true;
    }
        // ?????

    public LoginDto(MemberEntity memberEntity, Collection<?extends GrantedAuthority> authorities){
        this.memberid = memberEntity.getMemberid();
        this.memberpassword = memberEntity.getMemberpassword();
        this.authorities = Collections.unmodifiableSet(new LinkedHashSet<>(authorities));
            // 현 코드가 LoginDto의 private final Set<GrantedAuthority> authorities; <-- 초기화를 시킴.
    }

}
