package com.playground.real_project_api;

import com.playground.real_project_api.utils.random.RandomStringGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RealProjectApiApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void 랜덤테스트(){
        String a=RandomStringGenerator.getSaltedRandomString(5);
        String digit = RandomStringGenerator.getRandomStringDigit(5);
        System.out.println(a);
        System.out.println(digit);
    }
}
