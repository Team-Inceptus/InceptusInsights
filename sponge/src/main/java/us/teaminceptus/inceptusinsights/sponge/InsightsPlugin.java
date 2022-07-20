package us.teaminceptus.inceptusinsights.sponge;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "inceptusinsights", name = "InceptusInsights", version = "1.0.0", description = "1.13+ Sponge Plugin for InceptusInsights", authors = "Team Inceptus")
public final class InsightsPlugin {

    @Listener
    public void onServerStart(GameStartedServerEvent e) {

    }

}
