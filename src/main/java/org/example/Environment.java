package org.example;

import io.github.cdimascio.dotenv.Dotenv;

public final class Environment {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getVariable(String key) {
        return dotenv.get(key);
    }
}
