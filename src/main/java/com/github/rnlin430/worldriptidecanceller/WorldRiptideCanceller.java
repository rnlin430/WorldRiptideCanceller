package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.MalformedURLException;
import java.net.URL;

public final class WorldRiptideCanceller extends JavaPlugin {

    public static boolean isEnable = true;
    public static String endMessage = "End message";
    public static String startMessage = "Start message";
    public static int tpsThreshold = 15;
    public static int updateFrequency = 40;
    private FileConfiguration config;


    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getScheduler().runTaskTimer(this, new RiptedCancellerTask(this), 0, updateFrequency);

        new com.github.rnlin430.worldriptidecanceller.RiptedListener(this);

        saveDefaultConfig();
        this.initialize();
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
        if (command.getName().equalsIgnoreCase("wr")) {
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
                case 2:
                    switch (args[0]){
                        case "setts":
                            config.set("tps_threshold", Integer.valueOf(args[1]).intValue());
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: tpsが " + args[1] + " 以下で激流付きトライデントを制限します。");
                            return true;
                        case "setuf":
                            config.set("update_frequency", Integer.valueOf(args[1]).intValue());
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: tpsのスキャン頻度が " + args[1] + " になりました。");
                            return true;
                        case "setstartmessage":
                            config.set("start_message", args[1]);
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: 開始メッセージを更新しました。");
                            return true;
                        case "setendmessage":
                            config.set("end_message", args[1]);
                            saveConfig();
                            reloadConfig();
                            sender.sendMessage(ChatColor.GRAY + "info: 終了メッセージを更新しました。");
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
        saveDefaultConfig();
        reloadConfig();
        config = getConfig();
        WorldRiptideCanceller.isEnable = config.getBoolean("Enable");
        WorldRiptideCanceller.tpsThreshold = config.getInt("tps_threshold");
        WorldRiptideCanceller.updateFrequency = config.getInt("update_frequency");
        WorldRiptideCanceller.startMessage = config.getString("start_message");
        WorldRiptideCanceller.endMessage = config.getString("end_message");
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
