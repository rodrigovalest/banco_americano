package com.rodrigovalest.ms_costumer.utils;

import java.util.UUID;

public class NameGenerator {
    public static String generate() {
        return System.currentTimeMillis() + "__" + UUID.randomUUID().toString();
    }
}
