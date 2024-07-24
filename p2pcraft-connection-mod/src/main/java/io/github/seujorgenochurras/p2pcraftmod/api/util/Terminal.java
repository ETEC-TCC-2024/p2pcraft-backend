package io.github.seujorgenochurras.p2pcraftmod.api.util;

import java.io.File;
import java.io.IOException;

public class Terminal {
    public static Process execute(String workingDirectory, String... command) {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File(workingDirectory));
        try {
            return processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}