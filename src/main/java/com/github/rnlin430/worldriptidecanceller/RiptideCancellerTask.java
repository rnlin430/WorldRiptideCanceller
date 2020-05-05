package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class RiptideCancellerTask extends BukkitRunnable {
    private WorldRiptideCanceller plugin;
    public static boolean isRestricted = true;
    private CommandSender sender;

    public RiptideCancellerTask(WorldRiptideCanceller plugin){
        this(plugin, null);
        this.plugin = plugin;
    }

    public RiptideCancellerTask(WorldRiptideCanceller plugin, CommandSender sender){
        this.sender = sender;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        TpsDataCollector rf = new TpsDataCollector(plugin);
        double[] tps = rf.getRecentTps();

        if(sender != null){
            sender.sendMessage(ChatColor.GOLD + "1ms:" + ChatColor.AQUA + "" + tps[0] +
                    ChatColor.GOLD + "5ms:" + ChatColor.AQUA + "" + tps[1] +
                    ChatColor.GOLD + "15ms:" + ChatColor.AQUA + "" + tps[2]);
            return;
        }

        if(isRestricted){
            if(tps[0] <= WorldRiptideCanceller.tpsThreshold){
                if (WorldRiptideCanceller.startMessage != null)
                plugin.getServer().broadcastMessage(WorldRiptideCanceller.startMessage);
                isRestricted = false;
                return;
            }
        }
        else if(!isRestricted) {
            if (tps[0] > WorldRiptideCanceller.tpsThreshold) {
                if (WorldRiptideCanceller.endMessage != null) {
                    plugin.getServer().broadcastMessage(WorldRiptideCanceller.endMessage);
                    isRestricted = true;
                }
                return;
            }
        }
    }
}
