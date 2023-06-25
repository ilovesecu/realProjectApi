package com.playground.real_project_api.api.controller;

import com.playground.real_project_api.proc.RealProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final RealProjectMapper mapper;

    @GetMapping(value = "/api/test")
    public void test(){
        System.out.println("TeST");
    }

    @GetMapping(value = "/security")
    public void secTest(){
        System.out.println("Test succ");
    }

    @PostMapping(value = "/token")
    public String token(){
        return "<h1>toekn</h1>";
    }
}
