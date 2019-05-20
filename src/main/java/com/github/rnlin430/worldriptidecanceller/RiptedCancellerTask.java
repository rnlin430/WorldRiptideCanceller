package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.scheduler.BukkitRunnable;

public class RiptedCancellerTask extends BukkitRunnable {
    private WorldRiptideCanceller plugin;
    static public boolean isRestricted = true;

    public RiptedCancellerTask(WorldRiptideCanceller plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Reflection rf = new Reflection(plugin);
        double[] tps = rf.getRecentTps();
        plugin.getServer().broadcastMessage("§2" + String.valueOf(tps[0]));
        plugin.getServer().broadcastMessage("§3" + String.valueOf(tps[1]));
        if(isRestricted){
            if(tps[0] < 18){
                plugin.getServer().broadcastMessage("§3激流の使用が制限されています。");
                System.out.println("§4激流の使用が制限されています。");
                isRestricted = false;
            }
        }
        else if(!isRestricted) {
            if (tps[0] >= 18)
                System.out.println("§3激流の使用が制限が解除されました。");
            isRestricted = true;
        }
    }
}
