package com.example.springsecurity.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {  // 열거형 : 상수, 식별자를 담은 자료형???
    Member("ROLE_MEMBER","회원"), // 부여 가능한 권한 정보
    SNS("ROLE_SNS","SNS회원"), // 부여 가능한 권한 정보
    Admin("ROLE_ADMIN","관리자");  // 상동


    private final String KEY;
    private final String KEYWORD;
}
