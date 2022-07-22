package us.teaminceptus.inceptusinsights.api.player;

import java.util.Map;

public interface PlayerInformation {

    default PlayerData getPlayerInformation(String name) {
        return getAllPlayers().get(name);
    }

    Map<String, PlayerData> getAllPlayers();

    Map<String, PlayerData> getOnlinePlayers();
}
