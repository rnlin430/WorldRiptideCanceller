package com.github.rnlin430.worldriptidecanceller;

import java.lang.reflect.Field;

public class TpsDataCollector {
    private com.github.rnlin430.worldriptidecanceller.WorldRiptideCanceller plugin;
    private double[] recentTps;

    public TpsDataCollector(com.github.rnlin430.worldriptidecanceller.WorldRiptideCanceller plugin){
        this.plugin = plugin;
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

    private Field getReflectionField(Object obj, String s){
        try{
            Field f = obj.getClass().getDeclaredField(s);
            f.setAccessible(true);
            return f;
        }catch(SecurityException e){
            e.printStackTrace();
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }

    private Field getReflectionSuperField(Object obj, String s){
        try{
            Field f = obj.getClass().getSuperclass().getDeclaredField(s);
            f.setAccessible(true);
            return f;
        }catch(SecurityException e){
            e.printStackTrace();
        }catch(NoSuchFieldException e){
            e.printStackTrace();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
        return null;
    }

    public Object getReflectionValue(Field f, Object obj){
        try {
            return f.get(obj);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
        return null;
    }

    public double[] getRecentTps() {
        Class<?> CraftServerClazz = getReflectionClass("org.bukkit.craftbukkit.v" +
                plugin. getServer().getClass().getPackage().getName().replaceFirst(".*(\\d+_\\d+_R\\d+).*", "$1") +
                "." + "CraftServer"); // CraftServerクラスのClassオブジェクトを取得
        Object getCraftServer = CraftServerClazz.cast(plugin.getServer()); // Class.cast()
        Field console = getReflectionField(getCraftServer, "console");
        Object getMinecraftServer = getReflectionValue(console, getCraftServer);
        Class<?> MinecraftServer = getReflectionClass("net.minecraft.server.v" +
                plugin.getServer().getClass().getPackage().getName().replaceFirst(".*(\\d+_\\d+_R\\d+).*", "$1") +
                "." + "MinecraftServer");
        Object castObj = MinecraftServer.cast(getMinecraftServer);
        Field field = getReflectionSuperField(castObj, "recentTps");
        recentTps = (double[]) getReflectionValue(field, castObj);
        return recentTps;
    }
}
