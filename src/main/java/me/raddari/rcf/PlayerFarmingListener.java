package me.raddari.rcf;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class PlayerFarmingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        var block = event.getClickedBlock();

        if (block != null && block.getBlockData() instanceof Ageable) {
            // Player right clicked a crop
            // Only allow harvesting of fully grown crops
            var crop = (Ageable) block.getBlockData();

            if (crop.getAge() == crop.getMaximumAge()) {
                var player = event.getPlayer();
                var world = player.getWorld();
                var material = block.getBlockData().getMaterial();

                block.breakNaturally();
                player.incrementStatistic(Statistic.MINE_BLOCK, material);
                world.getBlockAt(block.getLocation()).setType(seedType(material));
            }
        }
    }

    private static @NotNull Material seedType(@NotNull Material crop) {
        if (!CROP_LOOKUP.containsKey(crop)) {
            throw new IllegalArgumentException(String.format("Material does not have a seed type: %s", crop));
        }
        return CROP_LOOKUP.get(crop);
    }

    private static final Map<@NotNull Material, @NotNull Material> CROP_LOOKUP = Maps.newHashMap();
    static {
        CROP_LOOKUP.put(Material.WHEAT, Material.WHEAT);
        CROP_LOOKUP.put(Material.BEETROOT, Material.BEETROOTS);
        CROP_LOOKUP.put(Material.POTATO, Material.POTATOES);
        CROP_LOOKUP.put(Material.CARROT, Material.CARROTS);
    }

}
