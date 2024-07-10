package io.github.seujorgenochurras.p2pApi.common;

public class HostAndPort {
    private String host;
    private int port = 80;


    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Tries to automatically divide host from port
     *
     * @param ip the full ip
     */
    public HostAndPort(String ip) {
        int portSeparatorIndex = ip.lastIndexOf(":");

        if (portSeparatorIndex != -1) {
            this.host = ip.substring(0, portSeparatorIndex);
            this.port = Integer.parseInt(ip.substring(portSeparatorIndex + 1));
        } else {
            this.host = ip;
        }
    }

    public String getFullIp() {
        return getHost() + getPort();
    }

    public String getHost() {
        return host;
    }

    public HostAndPort setHost(String host) {
        this.host = host;
        return this;
    }

    public int getPort() {
        return port;
    }

    public HostAndPort setPort(int port) {
        this.port = port;
        return this;
    }
}
