package com.monstahhh.mrworldwide;

import com.monstahhh.config.Config;

import java.io.IOException;

public class MrWorldWide {

    public static long OwnerId = 257247527630274561L;

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World");
        Config conf = new Config().read();
        System.out.println(conf.getToken());
    }
}
