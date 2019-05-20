package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public final class WorldRiptideCanceller extends JavaPlugin {

    public static boolean isEnable    = true;
    public static String endMessage   = "End message";
    public static String startMessage = "Start message";
    public static double tpsThreshold = 15;
    public static int updateFrequency = 40;
    private FileConfiguration config;
    private static HashMap<Player, Integer> bukkitIdManager = new HashMap<Player, Integer>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.initialize();
        if(isEnable) {
            getServer().getScheduler().runTaskTimer(this, new RiptedCancellerTask(this), 0, updateFrequency);
        }
        new RiptedListener(this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /**
         *  AdminCommands
         */
        if (command.getName().equalsIgnoreCase("wrc")) {
            // 権限をチェック
            if (!sender.hasPermission("worldriptidecanceller.command.wr")) {
                sender.sendMessage(ChatColor.DARK_RED + command.getPermissionMessage());
                displayInfo(sender);
                return true;
            }
            switch (args.length) {
                case 0:
                    displayInfo(sender);
                    break;
                case 1:
                    if (args[0].equalsIgnoreCase("true")) {
                        config.set("Enable", true);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(ChatColor.GRAY + "info: WorldRiptideCancellerが有効になりました。");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("false")) {
                        config.set("Enable", false);
                        saveConfig();
                        reloadConfig();
                        sender.sendMessage(ChatColor.GRAY + "info: WorldRiptideCancellerが無効になりました。");
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("showp")) {
                        config = getConfig();
                        WorldRiptideCanceller.isEnable        = config.getBoolean("Enable");
                        WorldRiptideCanceller.tpsThreshold    = config.getDouble ("tps_threshold");
                        WorldRiptideCanceller.updateFrequency = config.getInt    ("update_frequency");
                        WorldRiptideCanceller.startMessage    = config.getString ("start_message");
                        WorldRiptideCanceller.endMessage      = config.getString ("end_message");
                        sender.sendMessage(ChatColor.GRAY + "enable: "           + ChatColor.AQUA + isEnable);
                        sender.sendMessage(ChatColor.GRAY + "tps_threshold: "    + ChatColor.AQUA + tpsThreshold);
                        sender.sendMessage(ChatColor.GRAY + "update_frequency: " + ChatColor.AQUA + updateFrequency);
                        sender.sendMessage(ChatColor.GRAY + "start_message: "    + ChatColor.AQUA + startMessage);
                        sender.sendMessage(ChatColor.GRAY + "end_message: "      + ChatColor.AQUA + endMessage);
                        saveConfig();
                        reloadConfig();
                        return true;
                    }

                    if(args[0].equalsIgnoreCase("reload")) {
                        this.initialize();
                        return true;
                    }
                    return true;
                case 2:
                    switch (args[0]){
                        case "setts":
                            tpsThreshold = Double.parseDouble(args[1]);
                            config = getConfig();
                            config.set("tps_threshold", Double.parseDouble(args[1]));
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: tpsが " + args[1] + " 以下で激流付きトライデントを制限します。");
                            return true;
                        case "setuf":
                            updateFrequency = Integer.valueOf(args[1]).intValue();
                            config = getConfig();
                            config.set("update_frequency", Integer.valueOf(args[1]).intValue());
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: tpsのスキャン頻度が " + args[1] + " になりました。");
                            return true;
                        case "setstartmessage":
                            startMessage = args[1];
                            config = getConfig();
                            config.set("start_message", args[1]);
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: 開始メッセージを更新しました。");
                            return true;
                        case "setendmessage":
                            endMessage = args[1];
                            config = getConfig();
                            config.set("end_message", args[1]);
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: 終了メッセージを更新しました。");
                            return true;
                        case "showtps":
                            Player player = (Player)sender;
                            if(args[1] == null) return true;
                            Integer id = new RiptedCancellerTask(this, sender).
                                    runTaskTimer(this,0,Integer.parseInt(args[1])).getTaskId();
                            bukkitIdManager.put(player, id);
                            return true;
                        case "hidetps":
                            Player player2 = (Player)sender;
                            if(args[1] == null) return true;
                            if(!this.bukkitIdManager.containsKey(player2)) return true;
                            Integer usb = this.bukkitIdManager.get(player2);
                            this.getServer().getScheduler().cancelTask(usb);
                            return true;
                    }
                    return true;
            }
            return true;
        }
        return true;
    }
    public void initialize(){
        // config
        this.saveDefaultConfig();
        this.reloadConfig();
        this.config = getConfig();
        WorldRiptideCanceller.isEnable        = this.config.getBoolean("Enable");
        WorldRiptideCanceller.tpsThreshold    = this.config.getDouble ("tps_threshold");
        WorldRiptideCanceller.updateFrequency = this.config.getInt    ("update_frequency");
        WorldRiptideCanceller.startMessage    = this.config.getString ("start_message");
        WorldRiptideCanceller.endMessage      = this.config.getString ("end_message");
        this.reloadConfig();
    }

    private void displayInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "- WorldRiptideCanceller -");
        sender.sendMessage(ChatColor.WHITE + "Spigotバージョン : 1.13.2");
        sender.sendMessage(ChatColor.WHITE + "Pluginバージョン : " + getDescription().getVersion());
        sender.sendMessage(ChatColor.RED + "ダウンロードURL : " + getSiteURL());
        sender.sendMessage(ChatColor.DARK_BLUE + "Developed by rnlin(Twitter: @rnlin)");
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "--------");
    }


    public void info(String s) {
        getLogger().info(s);
    }

    public URL getSiteURL() {
        URL url = null;
        try {
            url = new URL("https://github.com/rnlin430/");
        } catch (MalformedURLException e) {
            info(ChatColor.GRAY + "未設定です。");
        }
        return url;
    }
}
