package com.example.springsecurity.dto;

import com.example.springsecurity.domain.member.MemberEntity;
import com.example.springsecurity.domain.member.Role;
import lombok.*;

import java.util.Map;
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class Oauth2Dto {    // SNS로그인 사용자의 정보를 담기 위한 Dto class
    private String memberid;    // SNS email
    private String membername;  // naver : 실명, kakao : nickname
    private String registrationId;  // sns로그인 경로(naver, kakao 등)
    private Map<String, Object> attributes; // 실질적인 인증 정보(네이버 : response, 카카오 : kakao_account)가 담기는 Map
    private String nameAttributeKey;    // 인증 정보를 가져오기 위한 키값(response, kakao_account)

    // sns 로그인 경로 확인 후 알맞는 함수 호출, 변환하여 리턴하는 메서드
    public static Oauth2Dto snsCheck(String registrationId,Map<String, Object> attributes, String nameAttributeKey){
        if(registrationId.equals("naver")){ // 네이버 로그인 회원
            return convNaver(registrationId,attributes,nameAttributeKey);
        }else if(registrationId.equals("kakao")){   // 카카오 로그인 회원
            return convKakao(registrationId,attributes,nameAttributeKey);
        }else{
            return null;
        }
    }

    //////////////////////////////// 각 업체별 json 구조에 맞게 해석하여 dto를 빌드, 리턴 ////////////////////////////////
    // registrationId : 업체 구분용 값(업체 명칭)
    // Map<String, Object> attributes : 업체에서 전달받은 회원정보가 담긴 K:V
    // nameAttributeKey : attributes에서 사용할 Key
    public static Oauth2Dto convNaver(String registrationId, Map<String, Object> attributes, String nameAttributeKey){
        Map<String ,Object> response = (Map<String, Object>) attributes.get(nameAttributeKey);
        return Oauth2Dto.builder()
                .memberid(String.valueOf(response.get("email")))
                .membername(String.valueOf(response.get("name")))
                .registrationId(registrationId)
                .build();
    }

    public static Oauth2Dto convKakao(String registrationId, Map<String, Object> attributes, String nameAttributeKey){
        Map<String ,Object> kakao_account = (Map<String, Object>) attributes.get(nameAttributeKey);
        return Oauth2Dto.builder()
                .memberid(String.valueOf(kakao_account.get("email")))
                .membername(String.valueOf(kakao_account.get("profile")))
                .registrationId(registrationId)
                .build();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public MemberEntity toEntity(){ // DTO-> MemberEntity화
        return MemberEntity.builder()
                .memberid(this.memberid)
                .role(Role.SNS)  // 권한 : SNS회원 권한 부여
                .build();
    }

}
