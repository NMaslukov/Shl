package com.pro.utils;

import java.io.FileWriter;
import java.io.IOException;

public class Writer {

    public static void appendTo(String path, String text) {
        write(path, text, true);
    }

    public static void overwriteTo(String path, String text) {
        write(path, text, false);
    }

    private static void write(String path, String text, boolean append) {
        try(FileWriter writer = new FileWriter(path, append)){
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
