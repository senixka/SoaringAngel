package com.mygdx.game.MapGenerator;

import java.security.SecureRandom;

public class Rand {

    private static SecureRandom secureRandom = new SecureRandom();

    public static int ModInt(int mod) {
        return secureRandom.nextInt() % mod;
    }

    public static int AbsModInt(int mod) {
        return Math.abs(secureRandom.nextInt()) % mod;
    }
}
