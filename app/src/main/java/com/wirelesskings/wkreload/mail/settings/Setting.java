package com.wirelesskings.wkreload.mail.settings;

public class Setting {
    private String host;
    private String username;
    private String password;
    private int serverType;
    private int port;

    public Setting() {
    }

    public Setting(String host) {
        this.host = host;
    }

    public Setting(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Setting(String host, int serverType, int port) {
        this.host = host;
        this.serverType = serverType;
        this.port = port;
    }

    public Setting(String host, String username, String password, int serverType, int port) {
        this.host = host;
        this.username = username;
        this.password = password;
        this.serverType = serverType;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}