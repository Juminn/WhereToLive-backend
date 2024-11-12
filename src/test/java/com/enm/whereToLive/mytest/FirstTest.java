package com.enm.whereToLive.mytest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


public class FirstTest {

    @Test
    public  void test1(){
        System.out.println("Hi");
        Assertions.assertSame(1,1);
    }

    @Test
    public  void test2(){
        System.out.println("Hi");
        Assertions.assertSame(1,1);
    }
}
