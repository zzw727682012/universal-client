package com.create.zzw;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ZzwApplicationTests {

    @Test
    void contextLoads() {
        ExecutorService executorService = Executors.newCachedThreadPool();
    }


    public static void main(String[] args) {
        System.out.println(Code1.test(5,0));
    }

}
