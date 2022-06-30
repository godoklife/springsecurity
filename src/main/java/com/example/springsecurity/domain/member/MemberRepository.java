package com.example.springsecurity.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    Optional<MemberEntity> findByMemberid(String memberid);
        // 1. Optional : null값을 수용할 수 있는 클래스 -> 안정성 향상 목적
        // 2. findBy인수(데이터형 인수명) : select * from entity where 인수

}
