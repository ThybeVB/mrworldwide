package com.monstahhh.mrworldwide;

import com.monstahhh.config.ConfigReader;

import java.io.IOException;

public class MrWorldWide {

    public static long OwnerId = 257247527630274561L;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");
        new ConfigReader().read();
    }
}
