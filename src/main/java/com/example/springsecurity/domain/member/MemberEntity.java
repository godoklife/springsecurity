package com.example.springsecurity.domain.member;

import lombok.*;

import javax.persistence.*;

@Entity // 엔티티 클래스 지정
@Table(name = "member") // 테이블 지정, 테이블명 : member, default : name_entity
@Getter // field.get
@Setter // field.set
@ToString   // toString()
@Builder    // 생성자보다 높은 안정성을 위한 Builder 주입
@AllArgsConstructor // 풀 생성자 생성
@NoArgsConstructor  // 빈 생성자 생성
public class MemberEntity {
    @Id // PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int memberno;
    @Column // 컬럼 지정, default : varchar(255)
    private String memberid;
    @Column
    private String memberpassword;
    @Column
    private String membernickname;
    @Column
    private String memberemail;
    @Column
    private String auth;
    @Column @Enumerated(EnumType.STRING)    // SQL에 권한을 저장할 데이터 형태 지정
    private Role role;
}
