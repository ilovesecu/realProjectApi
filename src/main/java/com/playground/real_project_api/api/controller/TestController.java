package com.playground.real_project_api.api.controller;

import com.playground.real_project_api.proc.RealProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final RealProjectMapper mapper;

    @GetMapping(value = "/api/test")
    public void test(){
        mapper.test();
    }
}
