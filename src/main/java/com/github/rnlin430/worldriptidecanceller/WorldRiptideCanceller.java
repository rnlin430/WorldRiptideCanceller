package com.github.rnlin430.worldriptidecanceller;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;

import java.io.Console;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public final class WorldRiptideCanceller extends JavaPlugin {

    public static boolean   isEnable            = true;
    public static String    endMessage          = null;
    public static String    startMessage        = null;
    public static String    cancelMessage       = null;
    public static double    tpsThreshold        = 17;
    public static int       updateFrequency     = 20 * 60;
    private static WorldRiptideCanceller instance;
    private FileConfiguration config;
    private static HashMap<Player, Integer> bukkitIdManager = new HashMap<>();
    private BukkitTask bukkitTask = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.initialize();

        new RiptideListener(this);

        PluginManager pm = Bukkit.getPluginManager();

        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         // AdminCommands
        if (command.getName().equalsIgnoreCase("wrc")) {
            // 権限をチェック
            if (!sender.hasPermission("worldriptidecanceller.command.wr")) {
                displayInfo(sender);
                sender.sendMessage(ChatColor.DARK_RED + command.getPermissionMessage());
                return true;
            }
            switch (args.length) {
                case 0:
                    displayInfo(sender);
                    return true;
                case 1:
                    if (args[0].equalsIgnoreCase("true")) {
                        isEnable = true;
                        config = getConfig();
                        config.set("enable", true);
                        saveConfig();
                        reloadConfig();
                        initialize();
                        sender.sendMessage(ChatColor.GRAY + "[INFO] WorldRiptideCancellerが有効になりました。");
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("false")) {
                        isEnable = false;
                        config = getConfig();
                        config.set("enable", false);
                        saveConfig();
                        reloadConfig();
                        initialize();
                        sender.sendMessage(ChatColor.GRAY + "[INFO] WorldRiptideCancellerが無効になりました。");
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("info")) {
                        reloadConfig();
                        config = getConfig();
                        initialize();
                        sender.sendMessage(ChatColor.GRAY + "[" +  ChatColor.BLUE + "WRC" + ChatColor.GRAY + "]");
                        sender.sendMessage(ChatColor.GRAY + "enable: "           + ChatColor.AQUA + isEnable);
                        sender.sendMessage(ChatColor.GRAY + "tps_threshold: "    + ChatColor.AQUA + tpsThreshold);
                        sender.sendMessage(ChatColor.GRAY + "update_frequency: " + ChatColor.AQUA + updateFrequency);
                        sender.sendMessage(ChatColor.GRAY + "start_message: "    + ChatColor.AQUA + startMessage);
                        sender.sendMessage(ChatColor.GRAY + "end_message: "      + ChatColor.AQUA + endMessage);
                        sender.sendMessage(ChatColor.GRAY + "cancel_message: "   + ChatColor.AQUA + cancelMessage);
                        return true;
                    }

                    if(args[0].equalsIgnoreCase("reload")) {
                        reloadConfig();
                        this.initialize();
                        sender.sendMessage(ChatColor.GRAY + "[INFO] リロードしました。");
                        if (sender instanceof ConsoleCommandSender) return true;
                        info("リロードしました。");
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("hidetps")) {
                        Player player2 = (Player) sender;
                        if (this.bukkitIdManager.containsKey(player2)) {
                            Integer usb = this.bukkitIdManager.get(player2);
                            this.bukkitIdManager.remove(player2);
                            this.getServer().getScheduler().cancelTask(usb);
                            sender.sendMessage(ChatColor.GRAY + "[INFO] リロードしました。");
                            return true;
                        }
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("commands")) {
                        sender.sendMessage(ChatColor.WHITE + "/wrc commands");
                        sender.sendMessage(ChatColor.AQUA + "コマンド一覧。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc [true|false]");
                        sender.sendMessage(ChatColor.AQUA + "trueで激流に制限がかかります。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc info");
                        sender.sendMessage(ChatColor.AQUA + "現在の設定値です。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc reload");
                        sender.sendMessage(ChatColor.AQUA + "configをリロードします。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc setts <閾値>");
                        sender.sendMessage(ChatColor.AQUA + "閾値を下回ったら制限を開始します。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc setuf <tpsスキャン頻度>");
                        sender.sendMessage(ChatColor.AQUA + "TPS");
                        sender.sendMessage(ChatColor.WHITE + "/wrc setstartmessage <スタートメッセージ>");
                        sender.sendMessage(ChatColor.AQUA + "制限開始時のメッセージを編集します。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc setendmessage <エンドメッセージ>" );
                        sender.sendMessage(ChatColor.AQUA + "制限終了時のメッセージを編集します。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc setcancelmessage <キャンセルメッセージ>" );
                        sender.sendMessage(ChatColor.AQUA + "激流キャンセル時のメッセージを編集します。");
                        sender.sendMessage(ChatColor.WHITE + "/wrc showtps <更新頻度>");
                        sender.sendMessage(ChatColor.AQUA + "現在のtpsを指定更新頻度で表示し続けます。");
                        return true;
                    }
                    if(args[0].equalsIgnoreCase("showtps")) {
                            Player player = (Player) sender;
                            if(bukkitIdManager.containsKey(player)) {
                                int id = bukkitIdManager.get(player);
                                this.getServer().getScheduler().cancelTask(id);
                                bukkitIdManager.remove(player);
                                sender.sendMessage(ChatColor.GRAY + "[INFO] tps表示をオフにしました。");
                                return true;
                            }
                            bukkitIdManager.put(player, tpsTask(sender, 200));
                            return true;
                    }
                    return false;
                    case 2:
                        switch (args[0]) {
                            case "setts":
                                tpsThreshold = Double.parseDouble(args[1]);
                                config = getConfig();
                                config.set("tps_threshold", Double.parseDouble(args[1]));
                                saveConfig();
                                reloadConfig();
                                initialize();
                                sender.sendMessage(ChatColor.GRAY + "[INFO] tpsが " + tpsThreshold + " 以下で激流付きトライデントを制限します。");
                                return true;
                            case "setuf":
                                updateFrequency = Integer.valueOf(args[1]).intValue();
                                config = getConfig();
                                config.set("update_frequency", Integer.valueOf(args[1]).intValue());
                                saveConfig();
                                reloadConfig();
                                initialize();
                                sender.sendMessage(ChatColor.GRAY + "[INFO] tpsのスキャン頻度が " + updateFrequency + " になりました。");
                                return true;
                            case "setstartmessage":
                                String sm = args[1].replace("&", "§");
                                if(sm.equalsIgnoreCase("none")) {
                                    config = getConfig();
                                    config.set("start_message", null);
                                } else {
                                    config = getConfig();
                                    config.set("start_message", sm);
                                }
                                saveConfig();
                                initialize();
                                sender.sendMessage(ChatColor.GRAY + "[INFO] 開始メッセージを更新しました。");
                                return true;
                            case "setendmessage":
                                String em = args[1].replace("&", "§");
                                if (em.equalsIgnoreCase("none")) {
                                    config = getConfig();
                                    config.set("end_message", null);
                                } else {
                                    config = getConfig();
                                    config.set("end_message", em);
                                }

                                saveConfig();
                                initialize();
                                sender.sendMessage(ChatColor.GRAY + "[INFO] 終了メッセージを更新しました。");
                                return true;
                            case "setcancelmessage":
                                String cm = args[1].replace("&", "§");
                                if (cm.equalsIgnoreCase("none")) {
                                    config = getConfig();
                                    config.set("cancel_message",  null);
                                } else {
                                    config = getConfig();
                                    config.set("cancel_message", cm);
                                }
                                saveConfig();
                                initialize();
                                sender.sendMessage(ChatColor.GRAY + "[INFO] キャンセル時のメッセージを更新しました。");
                                return true;
                            case "showtps":
                                Player player = (Player) sender;
                                if (args[1] == null) return true;
                                if(bukkitIdManager.containsKey(player)) {
                                    sender.sendMessage(ChatColor.GRAY + "[INFO] 既に表示しています。\n/wrc showtps で一度オフにしてから実行してください。");
                                    return true;
                                }
                                bukkitIdManager.put(player, tpsTask(sender, Integer.parseInt(args[1])));
                                return true;
                            }
                            return false;
                    }
                return false;
        }
        return false;
    }
    public void initialize() {
        // config
        this.saveDefaultConfig();
        this.reloadConfig();
        this.config = getConfig();
        isEnable          = this.config.getBoolean("enable");
        tpsThreshold      = this.config.getDouble ("tps_threshold");
        updateFrequency   = this.config.getInt    ("update_frequency");
        startMessage      = this.config.getString ("start_message", null);
        endMessage        = this.config.getString ("end_message", null);
        cancelMessage     = this.config.getString ("cancel_message", null);
        this.reloadConfig();
        if (bukkitTask != null)
            if (!bukkitTask.isCancelled())
                bukkitTask.cancel();
        bukkitTask = new RiptideCancellerTask(this).runTaskTimer(this, updateFrequency, updateFrequency);
    }

    private int tpsTask (CommandSender sender, int cycle) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                TpsDataCollector rf = new TpsDataCollector(WorldRiptideCanceller.getInstance());
                double[] tps = rf.getRecentTps();

                if(sender != null){
                    sender.sendMessage(ChatColor.GOLD + "1ms:" + ChatColor.AQUA + "" + tps[0] +
                            ChatColor.GOLD + "5ms:" + ChatColor.AQUA + "" + tps[1] +
                            ChatColor.GOLD + "15ms:" + ChatColor.AQUA + "" + tps[2]);
                    return;
                }
            }
        }.runTaskTimer(this, 0, cycle).getTaskId();
    }

    private void displayInfo(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "- WorldRiptideCanceller -");
        sender.sendMessage(ChatColor.WHITE + "SpigotAPIバージョン: " + getDescription().getAPIVersion());
        sender.sendMessage(ChatColor.WHITE + "Pluginバージョン: " + getDescription().getVersion());
        sender.sendMessage(ChatColor.WHITE + "コマンド一覧: " + ChatColor.AQUA  + "/wrc commands");
        sender.sendMessage(ChatColor.GREEN + "ダウンロードURL: " + getSiteURL());
        sender.sendMessage(ChatColor.BLUE + "Developed by rnlin(Twitter: @rnlin)");
        sender.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "--------");
    }

    public void info(String s) {
        getLogger().info(s);
    }

    @Nullable
    public static WorldRiptideCanceller getInstance() {
        return instance;
    }

    public URL getSiteURL() {
        URL url = null;
        try {
            url = new URL("https://github.com/rnlin430/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            info(ChatColor.GRAY + "未設定です。");
        }
        return url;
    }
}
