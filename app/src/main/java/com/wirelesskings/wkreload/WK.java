package com.wirelesskings.wkreload;

import com.wirelesskings.data.model.internal.RealmCredentials;
import com.wirelesskings.data.model.internal.RealmServerConfig;
import com.wirelesskings.wkreload.domain.model.internal.Credentials;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;

/**
 * Created by Alberto on 04/11/2017.
 */

public class WK {
    private static final WK ourInstance = new WK();

    private CredentialsStore credentialsStore;

    private WKSDK wksdk;

    public static WK getInstance() {
        return ourInstance;
    }

    private WK() {
        credentialsStore = new CredentialsStore();
    }

    public WKSDK createWKSession(ServerConfig serverConfig) {
        if (serverConfig != null)
            return new WKSDK(serverConfig);
        return null;
    }

    public WKSDK getWKSessionDefault() {
        if (wksdk == null)
            wksdk = createWKSession(getCredentials());
        return wksdk;
    }

    public boolean hasCredentials() {
        return getCredentials() != null;
    }

    public ServerConfig getCredentials() {
        RealmServerConfig realmServerConfig = credentialsStore.getCredentials();
        ServerConfig serverConfig = null;
        if (realmServerConfig != null)
            serverConfig = new ServerConfig()
                    .setActive(realmServerConfig.isActive())
                    .setEmail(realmServerConfig.getEmail())
                    .setPassword(realmServerConfig.getPassword())
                    .setCredentials(new Credentials()
                            .setUsername(realmServerConfig
                                    .getRealmCredentials()
                                    .getUsername())
                            .setPassword(realmServerConfig
                                    .getRealmCredentials()
                                    .getPassword())
                            .setToken(realmServerConfig
                                    .getRealmCredentials()
                                    .getToken()));
        return serverConfig;
    }

    public void replaceWKSession(WKSDK wksdk) {
        this.wksdk = wksdk;
        saveCredentials(wksdk.getServerConfig());
    }

    public void saveCredentials(ServerConfig serverConfig) {
        RealmServerConfig realmServerConfig = new RealmServerConfig()
                .setEmail(serverConfig.getEmail())
                .setActive(serverConfig.isActive())
                .setPassword(serverConfig.getPassword())
                .setRealmCredentials(new RealmCredentials()
                        .setUsername(serverConfig.getCredentials().getUsername())
                        .setPassword(serverConfig.getCredentials().getPassword())
                        .setToken(serverConfig.getCredentials().getToken()));

        credentialsStore.put(realmServerConfig);
    }

}
