package me.raddari.rcf;

import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public final class PlayerFarmingListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerFarmingListener.class);

    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        var block = event.getClickedBlock();
        var player = event.getPlayer();
        var item = event.getItem();

        if (block != null && VALID_HARVESTABLES.contains(block.getType())) {
            if (item == null || !item.getType().isBlock()) {
                // Player right clicked a crop
                // Only allow harvesting of fully grown crops
                var crop = (Ageable) block.getBlockData();
                LOGGER.debug("{} right click farm block {}", player.getName(), crop);

                if (crop.getAge() == crop.getMaximumAge()) {
                    LOGGER.debug("{} is harvesting block", player.getName());
                    doHarvest(player, player.getWorld(), block);
                }
            }
        }
    }

    private static void doHarvest(@NotNull Player player, @NotNull World world, @NotNull Block block) {
        var material = block.getBlockData().getMaterial();
        block.breakNaturally();
        player.incrementStatistic(Statistic.MINE_BLOCK, material);
        world.getBlockAt(block.getLocation()).setType(material);
        player.incrementStatistic(Statistic.USE_ITEM, seedType(material));
    }

    private static @NotNull Material seedType(@NotNull Material crop) {
        if (!SEED_LOOKUP.containsKey(crop)) {
            var errorMsg = String.format("Material %s does not have a seed type", crop);
            LOGGER.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
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
