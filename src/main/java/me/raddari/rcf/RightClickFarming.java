package me.raddari.rcf;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RightClickFarming extends JavaPlugin {

    @Override
    public void onEnable() {
        Listener[] listeners = {
                new PlayerFarmingListener()
        };

        for (var listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
