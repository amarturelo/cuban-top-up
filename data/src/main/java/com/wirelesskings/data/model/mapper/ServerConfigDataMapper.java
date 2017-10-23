package com.wirelesskings.data.model.mapper;

import com.wirelesskings.data.model.internal.RealmServerConfig;
import com.wirelesskings.wkreload.domain.model.internal.ServerConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alberto on 18/10/2017.
 */

public class ServerConfigDataMapper implements DataMapper<RealmServerConfig, ServerConfig> {

    @Override
    public List<ServerConfig> transform(List<RealmServerConfig> realmServerConfigs) {
        List<ServerConfig> serverConfigs = new ArrayList<>();
        if (realmServerConfigs != null)
            realmServerConfigs.stream().forEach(realmServerConfig -> serverConfigs.add(transform(realmServerConfig)));
        return serverConfigs;
    }

    @Override
    public ServerConfig transform(RealmServerConfig realmServerConfig) {
        ServerConfig serverConfig = new ServerConfig();
        if (realmServerConfig != null) {
            serverConfig.setEmail(realmServerConfig.getEmail());
            serverConfig.setPassword(realmServerConfig.getPassword());
        }
        return serverConfig;
    }
}
