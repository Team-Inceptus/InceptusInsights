package us.teaminceptus.inceptusinsights.bukkit;

import us.teaminceptus.inceptusinsights.api.player.PlayerData;
import us.teaminceptus.inceptusinsights.api.player.PlayerInformation;

import java.util.Map;

class BukkitPlayerInformation implements PlayerInformation {

    private final InsightsPlugin plugin;

    BukkitPlayerInformation(InsightsPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public Map<String, PlayerData> getAllPlayers() {
        return null;
    }

    @Override
    public Map<String, PlayerData> getOnlinePlayers() {
        return null;
    }
}
