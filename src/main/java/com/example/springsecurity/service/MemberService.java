package com.example.springsecurity.service;

import com.example.springsecurity.domain.member.MemberEntity;
import com.example.springsecurity.domain.member.MemberRepository;
import com.example.springsecurity.dto.LoginDto;
import com.example.springsecurity.dto.MemberDto;
import com.example.springsecurity.dto.Oauth2Dto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service // 현 클래스를 서비스 클래스로 지정
public class MemberService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {
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

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // SNS로그인 인증 결과 호출용 오어쓰2써비스 호출
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        // 로그인 인증 결과 저장
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 로그인 업체(구글, 다음, 애플, 네이버 등등) 구분용 코드 저장
        String registrationId = userRequest
                .getClientRegistration()
                .getRegistrationId();    // naver,kakao 구분용 정보

        // 업체에서 받아온 json에서 회원정보가 담긴 key값 저장(response, kakako_account, 등등...)
        String nameAttributekey = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        System.out.println("클라이언트 등록 이름 : "+registrationId);
        System.out.println("회원 정보(json) 호출시 사용되는 키 : "+nameAttributekey);
        System.out.println("인증 정보(로그인)결과 내용 : "+oAuth2User.getAttributes());

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();  // 권한을 담을 collections 생성
        authorities.add(new SimpleGrantedAuthority("SNS_TMP")); // 권한 부여
        Map<String , Object > attributes = oAuth2User.getAttributes();  // 업체로부터 받은 회원 정보를 담을 Map 생성

        Oauth2Dto oauth2Dto = Oauth2Dto.snsCheck(registrationId,attributes,nameAttributekey);
            // 업체별로 응답 형태가 조금 다름
            // 업체(구조)에 맞게 해석 후 dto화

        Optional<MemberEntity> optionalMember = memberRepository.findByMemberid(oauth2Dto.getMemberid());
            // db에서 memberid(현 코드에서는 email값)를 가지고 검색
        if ( ! optionalMember.isPresent()){    // 처음 로그인하는 사용자라면
            memberRepository.save(oauth2Dto.toEntity());    // DB에 정보 저장
        }
        return new DefaultOAuth2User(authorities,attributes,nameAttributekey);
            // 사용자 인증 정보를 AuthenticationFilter -> HttpSecurity 로 리턴

    }

    // 회원의 아이디명을 가져오기 위한 메서드
    public String callAuthResult(){
        String memberid=null;

        // 세션에서 회원 인증정보를 불러오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();   // 인증값 저장

        if(principal!=null){
            // 인증값이 있는 상태
            if(principal instanceof UserDetails){   // UserDetails를 상속받은 경우 -> 홈페이지 가입 회원인 경우
                memberid = ((UserDetails) principal).getUsername();
            }else if (principal instanceof OAuth2User){ // OAuth2User를 상속받은 경우 -> SNS회원인 경우
                Map<String, Object>attributes = ((OAuth2User) principal).getAttributes();
                if (attributes.get("response")!=null){  // 네이버 회원이면
                    Map<String ,Object> map = (Map<String, Object>) attributes.get("response");
                    memberid = (String) map.get("email");
                }else if(attributes.get("kakao_account")!=null){    // 카카오 회원이면
                    Map<String ,Object> map = (Map<String, Object>) attributes.get("kakao_account");
                    memberid = (String) map.get("email");
                }
            }
            return memberid;
        }else { //인증값이 없는 상태

            return memberid;
        }
    }
}
