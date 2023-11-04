package com.example.androidservice;

import java.util.Random;

public class RandomGenerator {
    public static int getRandomNumber(int min, int max) throws InterruptedException {
        Thread.sleep(1000);
        return new Random().nextInt(max) + min;
    }
}
