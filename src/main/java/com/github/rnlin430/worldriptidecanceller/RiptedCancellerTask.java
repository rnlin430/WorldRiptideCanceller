package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.scheduler.BukkitRunnable;

public class RiptedCancellerTask extends BukkitRunnable {
    private WorldRiptideCanceller plugin;
    public static boolean isRestricted = true;

    public RiptedCancellerTask(WorldRiptideCanceller plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        TpsDataCollector rf = new TpsDataCollector(plugin);
        double[] tps = rf.getRecentTps();
        plugin.getServer().broadcastMessage("§2" + String.valueOf(tps[0]));
        plugin.getServer().broadcastMessage("§3" + String.valueOf(tps[1]));
        if(isRestricted){
            if(tps[0] <= WorldRiptideCanceller.tpsThreshold){
                plugin.getServer().broadcastMessage(WorldRiptideCanceller.startMessage);
                isRestricted = false;
            }
        }
        else if(!isRestricted) {
            if (tps[0] > WorldRiptideCanceller.tpsThreshold)
                plugin.getServer().broadcastMessage(WorldRiptideCanceller.endMessage);
            isRestricted = true;
        }
    }
}
