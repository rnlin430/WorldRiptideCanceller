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
//
//    @EventHandler
//    public void onTridentClick(PlayerInteractEvent e) {
//        if(!WorldRiptideCanceller.isEnable) return;
//        if(RiptideCancellerTask.isRestricted) return;
//        Player player = e.getPlayer();
//        //if(player.isOp()) return;
//        Action ac = e.getAction();
//        if(!(ac == Action.RIGHT_CLICK_AIR || ac == Action.RIGHT_CLICK_BLOCK)) return;
//        ItemStack is = player.getInventory().getItemInMainHand();
//        if (is.getType() != Material.TRIDENT) return;
//        Map<Enchantment, Integer> ec = is.getEnchantments();
//        for(Enchantment enchantment : ec.keySet()) {
//            if (enchantment.equals(RIPTIDE)) {
//                e.setCancelled(true);
//                new BukkitRunnable() {
//
//                    @Override
//                    public void run() {
//                        System.out.println("run");
//                        e.getPlayer().setVelocity(new Vector(0,0,0));
//                    }
//                }.runTaskLater(plugin, 1);
//
//                break;
//            }
//        }
//    }

    @EventHandler
    public void onPlayerRiptide (PlayerRiptideEvent e) {
        System.out.println("PlayerRiptideEvent");
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
                player.sendMessage(WorldRiptideCanceller.cancelMessage);
            }
        }.runTaskLater(plugin, 1);
    }
}

