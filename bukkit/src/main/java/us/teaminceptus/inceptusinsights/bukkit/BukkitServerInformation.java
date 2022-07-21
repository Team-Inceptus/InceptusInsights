package us.teaminceptus.inceptusinsights.bukkit;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Server;
import us.teaminceptus.inceptusinsights.api.DataURI;
import us.teaminceptus.inceptusinsights.api.ServerInformation;

import java.io.File;
import java.util.Map;

class BukkitServerInformation implements ServerInformation {

    private final InsightsPlugin plugin;

    BukkitServerInformation(InsightsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Map<String, Object> getServerInformation() {
        Server srv = plugin.getServer();
        return ImmutableMap.<String, Object>builder()
                .put("name", srv.getName())
                .build();
    }

    @Override
    public DataURI getServerIcon() {
        File f = new File(plugin.getDataFolder().getParentFile().getParentFile(), "server-icon.png");
        if (f.exists()) return new DataURI(f);
        return null;
    }

    @Override
    public String getServerName() {
        return plugin.getConfig().getString("server-name", plugin.getServer().getName());
    }

}
