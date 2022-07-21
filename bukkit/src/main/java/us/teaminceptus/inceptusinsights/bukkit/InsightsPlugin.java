package us.teaminceptus.inceptusinsights.bukkit;

import org.bukkit.plugin.java.JavaPlugin;
import us.teaminceptus.inceptusinsights.InsightsServer;

import java.io.File;
import java.net.Socket;

public final class InsightsPlugin extends JavaPlugin {

    private boolean portInUse() {
        try {
            new Socket("localhost", getConfig().getInt("port", 30325)).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (portInUse()) {
            getLogger().severe("Port " + getConfig().getInt("port", 30325) + " is already in use. Please choose a different port.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        InsightsServer.setInformationFetchers(new BukkitServerInformation(this), new BukkitPlayerInformation(this));
        InsightsServer.setLogger(getLogger());
        InsightsServer.start("--port=" + getConfig().getInt("port", 30325));
        getLogger().info("Loaded InceptusInsights - Bukkit Version");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling Server...");
        if (InsightsServer.isRunning())
            InsightsServer.stop();
        else getLogger().info("Server is not running.");
    }

}
