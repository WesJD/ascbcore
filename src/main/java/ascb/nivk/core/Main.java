package ascb.nivk.core;

import java.util.logging.Level;

import ascb.nivk.core.arena.AbstractArena;
import ascb.nivk.core.classes.AbstractSCBClass;
import ascb.nivk.core.commands.ASCBCmd;
import ascb.nivk.core.commands.ClassCmd;
import ascb.nivk.core.commands.GiveRankCmd;
import ascb.nivk.core.commands.InfoCmd;
import ascb.nivk.core.listeners.GlobalListeners;
import ascb.nivk.core.player.PlayerManager;
import ascb.nivk.core.util.InstanceManager;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import ascb.nivk.core.doubleJump.DoubleJump;

public class Main extends JavaPlugin implements Listener {

    public static final String LOBBY_WORLD = "old_lobby";
    public static final String ARENA_WORLD = "old_lobby";

    private static Main main;

    private final PlayerManager playerManager = new PlayerManager();
    private final InstanceManager<AbstractArena> arenaManager = new InstanceManager<>("ascb.nivk.core.arena.impl");
    private final InstanceManager<AbstractSCBClass> classManager = new InstanceManager<>("ascb.nivk.core.classes");

    private Location lobbySpawn = new Location(Bukkit.getWorld(LOBBY_WORLD), 0, 50, 0);
    private Permission permissions;
    private BukkitTask announcer;

    @Override
    public void onLoad() {
        main = this;
    }

    @Override
    public void onEnable() {
        if (setupPermissions()) {
            final PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(new GlobalListeners(), this);
            pluginManager.registerEvents(new DoubleJump(), this);

            getCommand("ascb").setExecutor(new ASCBCmd());
            getCommand("class").setExecutor(new ClassCmd());
            getCommand("giverank").setExecutor(new GiveRankCmd());
            getCommand("info").setExecutor(new InfoCmd());

            announcer = new Announcer(this).runTaskTimer(this, 20, 1200);

            final FileConfiguration config = getConfig();
            config.addDefault("spawn_location", "54x149x331x1x90");
            config.options().copyDefaults(true);
            saveConfig();

            String[] lobbySpawnParts = config.getString("spawn_location").split("x");
            getLogger().info(lobbySpawnParts[0] + " " + " " + lobbySpawnParts[1] + " " + lobbySpawnParts[2] + " " + lobbySpawnParts[3]);
            double x = Double.parseDouble(lobbySpawnParts[0].replaceAll("x", ""));
            double y = Double.parseDouble(lobbySpawnParts[1].replaceAll("x", ""));
            double z = Double.parseDouble(lobbySpawnParts[2].replaceAll("x", ""));
            lobbySpawn = new Location(Bukkit.getWorld(LOBBY_WORLD), x, y, z);
            lobbySpawn.setPitch(Float.parseFloat(lobbySpawnParts[3].replaceAll("x", "")));
            lobbySpawn.setYaw(Float.parseFloat(lobbySpawnParts[4].replaceAll("x", "")));
        } else {
            getLogger().log(Level.SEVERE, "Unable to hook into Vault for permissions!");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        main = null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

    //what is even the point of these commands? -wes
    /*@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();

        if (cmd.equalsIgnoreCase("default")) {
            if (args[0] == "" || args.length != 1) {
                sender.sendMessage(tacc('&', "&cInvalid arguments"));
                return true;
            }
            Player p = Bukkit.getPlayer(args[0]);
            SCBPlayer scbPlayer = playerManager.getPlayer(p);
            if (permissions.has(p, "ascb.vip")) {
                permissions.playerRemove(p, "ascb.vip");
            }
            if (permissions.has(p, "ascb.gm")) {
                permissions.playerRemove(p, "ascb.gm");
            }
            if (p.isOp()) {
                sender.sendMessage(tacc('&', "&cPlayer is op"));
            }
            scbPlayer.recalculate();
            sender.sendMessage(tacc('&', "Defaulted player (Suc: " + (scbPlayer.getRank() == Rank.DEFAULT) + ")"));
            return true;
        }

        if (cmd.equalsIgnoreCase("recalculate")) {
            if (args[0] == "" || args.length != 1) {
                sender.sendMessage(tacc('&', "&cInvalid arguments"));
                return true;
            }
            if (sender instanceof Player) return (playerManager.getPlayer((Player) sender).getRank().getLevel() > 2);
            Player p = Bukkit.getPlayer(args[0]);
            playerManager.getPlayer(p).recalculate();
            sender.sendMessage(tacc('&', "&aRecalculated"));
            return true;
        }

        if (cmd.equalsIgnoreCase("contributors")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Contibutors: &e" + this.getDescription().getAuthors()));
            return true;
        }

        if (cmd.equalsIgnoreCase("ban")) {
            if (sender instanceof Player) return (playerManager.getPlayer((Player) sender).getRank().getLevel() > 2);
            if (args.length != 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInvalid arguments! &e/ban {PLAYER} {REASON}"));
                return true;
            }
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aBanned player &e" + args[0] + " &afor the reason: "));
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + args[1]));
            }
            Bukkit.getBanList(Type.NAME).addBan(args[0], args[1], null, sender.getName());
            if (Bukkit.getPlayer(args[0]) != null) {
                Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&aYou are banned by &e" + sender.getName() + " &afor the reason: &7" + args[1]));
            }
            return true;
        }

        if (cmd.equalsIgnoreCase("kick")) {
            if (sender instanceof Player) return (playerManager.getPlayer((Player) sender).getRank().getLevel() > 2);
            if (args.length != 2) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInvalid arguments! &e/kick {PLAYER} {REASON}"));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) != null) {
                Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&aYou are kicked by &e" + sender.getName() + " &afor the reason: &7" + args[1]));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have kicked &e" + args[0] + "&a for the reason:"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + args[1]));
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPlayer &e" + args[0] + "&a is not Online!"));
            }
            return true;
        }
        return false;
    }*/

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public InstanceManager<AbstractArena> getArenaManager() {
        return arenaManager;
    }

    public InstanceManager<AbstractSCBClass> getClassManager() {
        return classManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Permission getPermissions() {
        return permissions;
    }

    public static Main get() {
        return main;
    }
}
