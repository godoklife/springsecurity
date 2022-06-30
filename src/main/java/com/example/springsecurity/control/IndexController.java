package com.example.springsecurity.control;

import com.example.springsecurity.dto.MemberDto;
import com.example.springsecurity.service.MemberService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // 해당 클래스를 웹 페이지 경로 지정용 컨트롤러로 지정하는 어노테이션
public class IndexController {

    @Autowired MemberService memberService; // MemberService 공갈객체 선언

    @GetMapping("/")    // method : get, uri : / ->> main.html 페이지를 출력토록 함.
    public String main(){
        return "main";
    }

    @GetMapping("/memberonly")
    public String memberonly(){
        return "memberonly";
    }   // 상동

    @GetMapping("/adminonly")
    public String adminonly(){
        return "adminonly";
    }

    @GetMapping("/member/signup")
    public String signup(){
        return "member/signup";
    }

    @GetMapping("/member/login")
    public String login(){
        return "member/login";
    }

    @PostMapping("/member/signup-processing")
        // 1. method : post, uri : /member/signup-processing,
    public String signupProcessing(MemberDto memberDto){    // <form> 태그의 name을 기반으로 dto를 생성 후
        memberService.signup(memberDto);                    // memberService.signup()으로 dto를 전송
        return "/main";                                     // /main.html으로 이동

    }

}
