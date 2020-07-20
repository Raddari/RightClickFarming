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
import java.util.Set;

public final class PlayerFarmingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        var block = event.getClickedBlock();

        if (block != null && VALID_HARVESTABLES.contains(block.getType())) {
            // Player right clicked a crop
            // Only allow harvesting of fully grown crops
            var crop = (Ageable) block.getBlockData();

            if (crop.getAge() == crop.getMaximumAge()) {
                var player = event.getPlayer();
                var world = player.getWorld();
                var material = block.getBlockData().getMaterial();

                block.breakNaturally();
                player.incrementStatistic(Statistic.MINE_BLOCK, material);
                world.getBlockAt(block.getLocation()).setType(material);
                player.incrementStatistic(Statistic.USE_ITEM, seedType(material));
            }
        }
    }

    private static @NotNull Material seedType(@NotNull Material crop) {
        if (!SEED_LOOKUP.containsKey(crop)) {
            throw new IllegalArgumentException(String.format("Material does not have a seed type: %s", crop));
        }
        return SEED_LOOKUP.get(crop);
    }

    private static final Map<Material, Material> SEED_LOOKUP = Maps.newHashMap();
    static {
        SEED_LOOKUP.put(Material.WHEAT, Material.WHEAT_SEEDS);
        SEED_LOOKUP.put(Material.BEETROOTS, Material.BEETROOT_SEEDS);
        SEED_LOOKUP.put(Material.POTATOES, Material.POTATO);
        SEED_LOOKUP.put(Material.CARROTS, Material.CARROT);
        SEED_LOOKUP.put(Material.NETHER_WART, Material.NETHER_WART);
    }

    private static final Set<Material> VALID_HARVESTABLES = Set.copyOf(SEED_LOOKUP.keySet());

}
