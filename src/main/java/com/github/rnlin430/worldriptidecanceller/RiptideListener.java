package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Map;

import static org.bukkit.enchantments.Enchantment.RIPTIDE;

public class RiptideListener implements Listener {
    private WorldRiptideCanceller plugin;

    public RiptideListener(WorldRiptideCanceller plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerRiptide (PlayerRiptideEvent e) {
        if(!WorldRiptideCanceller.isEnable) return;
        if(RiptideCancellerTask.isRestricted) return;
        Player player = e.getPlayer();
        Location from = player.getLocation();
        if(player.hasPermission("worldriptidecanceller.ignoreriptidecancell")) return;
        new BukkitRunnable() {
            @Override
            public void run() {
                player.setVelocity(new Vector(0,0,0));
                player.teleport(from);
                if (WorldRiptideCanceller.cancelMessage == null) return;
                player.sendMessage(WorldRiptideCanceller.cancelMessage);
            }
        }.runTaskLater(plugin, 1);
    }
}

