package me.raddari.rcf;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RightClickFarming extends JavaPlugin {

    @SuppressWarnings("java:S1845") // 'logger' package protected field in JavaPlugin superclass
    private static final Logger LOGGER = LoggerFactory.getLogger(RightClickFarming.class);

    @Override
    public void onEnable() {
        LOGGER.info("RightClickFarming enabled!");
        Listener[] listeners = {
                new PlayerFarmingListener()
        };

        for (var listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        LOGGER.info("Goodbye :)");
    }

}
