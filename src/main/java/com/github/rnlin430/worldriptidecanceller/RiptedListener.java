package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Map;

import static org.bukkit.enchantments.Enchantment.RIPTIDE;

public class RiptedListener implements Listener {
    private WorldRiptideCanceller plugin;

    public RiptedListener(WorldRiptideCanceller plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onTridentClick(PlayerInteractEvent e) {
        if(!WorldRiptideCanceller.isEnable) return;
        if(RiptedCancellerTask.isRestricted) return;
        Player player = e.getPlayer();
        Action ac = e.getAction();
        if(!(ac == Action.RIGHT_CLICK_AIR || ac == Action.RIGHT_CLICK_BLOCK))return;
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType() != Material.TRIDENT) return;
        Map<Enchantment, Integer> ec = is.getEnchantments();
        for(Enchantment enchantment : ec.keySet()) {
            if (enchantment.equals(RIPTIDE)) {
                e.setCancelled(true);
                break;
            }
        }
    }
}

