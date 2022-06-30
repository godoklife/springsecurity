package com.example.springsecurity.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    Member("ROLE_MEMBER","회원");
    Admin("ROLE_ADMIN","관리자");
    private final String KEY;;
    private final String KEYWORD;
}
