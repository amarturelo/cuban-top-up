package com.wirelesskings.wkreload;

import com.wirelesskings.data.model.internal.RealmCredentials;
import com.wirelesskings.data.model.internal.RealmServerConfig;
import com.wirelesskings.wkreload.domain.model.internal.Credentials;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;
import com.wirelesskings.wkreload.mailmiddleware.Middleware;
import com.wirelesskings.wkreload.mailmiddleware.ResultListener;

import java.util.Map;

/**
 * Created by Alberto on 04/11/2017.
 */

public class WK {
    private static final WK ourInstance = new WK();


    private CredentialsStore credentialsStore;


    public static WK getInstance() {
        return ourInstance;
    }

    private WK() {
        credentialsStore = new CredentialsStore();
    }

    private ServerConfig serverConfig;

    private Middleware middleware;

    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    public boolean hasCredentials() {
        return credentialsStore.hasCredentials();
    }

    public ServerConfig getCredentials() {
        RealmServerConfig realmServerConfig = credentialsStore.getCredentials();

        ServerConfig serverConfig = new ServerConfig()
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

    public void saveCredentials(ServerConfig serverConfig) {
        RealmServerConfig realmServerConfig = new RealmServerConfig()
                .setEmail(serverConfig.getEmail())
                .setPassword(serverConfig.getPassword())
                .setRealmCredentials(new RealmCredentials()
                        .setUsername(serverConfig.getCredentials().getUsername())
                        .setPassword(serverConfig.getCredentials().getPassword())
                        .setToken(serverConfig.getCredentials().getToken()));

        credentialsStore.put(realmServerConfig);
    }

    public Middleware getMiddleware() {
        return middleware;
    }
}
