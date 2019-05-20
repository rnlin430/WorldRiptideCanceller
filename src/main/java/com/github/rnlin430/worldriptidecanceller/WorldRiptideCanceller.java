package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.plugin.java.JavaPlugin;

public final class WorldRiptideCanceller extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getScheduler().runTaskTimer(this, new RiptedCancellerTask(this), 0,40);

        new com.github.rnlin430.worldriptidecanceller.RiptedListener(this);
        System.out.println("org.bukkit.craftbukkit.v" + this. getServer().getClass().getPackage().getName().replaceFirst(".*(\\d+_\\d+_R\\d+).*", "$1") + "." + "CraftServer");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private Class<?> getReflectionClass(String s){
        Class<?> c = null;
        try{
            c = Class.forName(s);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        return c;
    }
}
