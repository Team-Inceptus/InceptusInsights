package us.teaminceptus.inceptusinsights.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.teaminceptus.inceptusinsights.api.player.PlayerInformation;

import java.util.HashMap;
import java.util.Map;

class BukkitPlayerInformation implements PlayerInformation {

    private final InsightsPlugin plugin;

    BukkitPlayerInformation(InsightsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Map<String, Object> getPlayerInformation(String name) {
        Player p = Bukkit.getPlayer(name);

        return new HashMap<>();
    }


}
