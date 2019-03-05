package com.anti_captcha.Helper;

import main.Logger;

import org.json.JSONObject;

public class DebugHelper {
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void setVerboseMode(Boolean verboseMode) {
        DebugHelper.verboseMode = verboseMode;
    }

    public enum Type {
        ERROR,
        INFO,
        SUCCESS
    }

    private static Boolean verboseMode = false;

    public static void jsonFieldParseError(String field, JSONObject submitResult) {
        String error = field + " could not be parsed. Raw response: " + JsonHelper.asString(submitResult);
        DebugHelper.out(error, DebugHelper.Type.ERROR);
    }

    public static void out(String message, Type type) {
        if (!verboseMode) {
            return;
        }

        if (type.equals(Type.ERROR)) {
            Logger.addMessage(ANSI_RED + message);
        }
        else if (type.equals(Type.INFO)) {
            Logger.addMessage(ANSI_YELLOW + message);
        }
        else if (type.equals(Type.SUCCESS)) {
           Logger.addMessage(ANSI_GREEN + message);
        }

        Logger.addMessage(ANSI_RESET);
    }
}
