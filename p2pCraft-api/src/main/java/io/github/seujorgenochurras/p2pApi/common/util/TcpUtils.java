package io.github.seujorgenochurras.p2pApi.common.util;

import io.github.seujorgenochurras.p2pApi.common.HostAndPort;

import java.io.IOException;
import java.net.Socket;

public class TcpUtils {

    private TcpUtils() {
    }

    public static boolean ping(HostAndPort hostAndPort) {
        String host = hostAndPort.getHost();
        int port = hostAndPort.getPort();
        try (Socket s = new Socket(host, port)) {
            return true;
        } catch (IOException ignored) {

        }

        return false;
    }
}
