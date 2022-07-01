package com.example.springsecurity.control;

import com.example.springsecurity.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 현 클래스를 객체반환 컨트롤러로 지정
@RequestMapping("/member")  // 경로 매핑 어노테이션 ->localhost:포트/member/이하경로
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/info")    // -> /member/info
    public String memberinfo(){
        return memberService.callAuthResult();
    }

}
