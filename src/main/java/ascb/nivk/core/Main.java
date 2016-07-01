package ascb.nivk.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import ascb.nivk.core.arena.Arena;
import ascb.nivk.core.arena.TestArena;
import ascb.nivk.core.classes.ClassRandom;
import ascb.nivk.core.classes.ClassSkeleton;
import ascb.nivk.core.classes.ClassZombie;
import ascb.nivk.core.player.PlayerManager;
import ascb.nivk.core.player.SCBPlayer;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.nametagedit.plugin.NametagEdit;

import ascb.nivk.core.doubleJump.DoubleJump;
import ascb.nivk.core.messages.OnJoin;

public class Main extends JavaPlugin implements Listener {

    public static final String LOBBY_WORLD = "old_lobby";
    public static final String ARENA_WORLD = "old_lobby";

    private static Main main;
    private final PlayerManager playerManager = new PlayerManager();

    private Location lobbySpawn = new Location(Bukkit.getWorld(LOBBY_WORLD), 0, 50, 0);
    private Permission perms;
    private BukkitTask announcer;
    public TestArena testarena;

    public List<PlayerClass> classes;

    @Override
    public void onLoad() {
        main = this;
    }

    @Override
    public void onEnable() {
        if (setupPermissions()) {
            final PluginManager pluginManager = getServer().getPluginManager();
            pluginManager.registerEvents(new OnJoin(), this);
            pluginManager.registerEvents(new DoubleJump(this), this);
            pluginManager.registerEvents(this, this);

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
            testarena = new TestArena(this);

            classes = new ArrayList<>();
            classes.add(new ClassZombie());
            classes.add(new ClassSkeleton());
        } else getLogger().log(Level.SEVERE, "Unable to hook into Vault for permission.");
    }

    @Override
    public void onDisable() {
        main = null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        final SCBPlayer scbPlayer = new SCBPlayer(player.getUniqueId(), player, this);

        playerManager.addPlayer(player, scbPlayer);

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setFireTicks(0);
        player.setFoodLevel(20);
        player.teleport(lobbySpawn);

        //getServer().getLogger().info("Made new SCB Player! UUID: " + players.get(players.size() - 1).getUuid() + " Array Size: " + players.size());

        scbPlayer.recalculate();
        NametagEdit.getApi().setPrefix(player, ChatColor.translateAlternateColorCodes('&', playerManager.getPlayer(player).getRank().getPrefix() + " "));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final SCBPlayer scbPlayer = playerManager.getPlayer(player);
        Arena.onPlayerLeave(scbPlayer);
        playerManager.removePlayer(player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cmd = command.getName();
        if (cmd.equalsIgnoreCase("ascb")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6The ASCB Project >> &cThis server is running ASCBCore " + this.getDescription().getVersion() + " &6made by&c " + this.getDescription().getAuthors()));
                return true;
            } else if(args.length == 1) {
                Arena.onPlayerJoin(playerManager.getPlayer((Player)sender), testarena);
            }
        }

        if (cmd.equalsIgnoreCase("pinfo")) {
            if (!(sender instanceof Player))
                return false;
            Player p3 = (Player) sender;
            SCBPlayer p = playerManager.getPlayer(p3);
            if (p == null) {
                sender.sendMessage("INVALID!");
                return true;
            }
            sender.sendMessage("UUID: " + p.getUuid() + " Class: " + p.getPlayerClass().getName() + " Is In Game: " + p.isInGame() + " Rank: " + p.getRank().getName());
        }

        if (cmd.equalsIgnoreCase("giverank")) {
            if (args[0] == "" || args[1] == "" || args.length != 2) {
                sender.sendMessage(tacc('&', "&cInvalid arguments"));
                return true;
            }
            if (sender instanceof Player) return (playerManager.getPlayer((Player) sender).getRank().getLevel() > 2);
            Player p = Bukkit.getPlayer(args[0]);
            perms.playerAdd(p, "ascb." + args[1].toLowerCase());
            playerManager.getPlayer(p).recalculate();
            sender.sendMessage(tacc('&', "&aRank given"));
            return true;
        }

        if (cmd.equalsIgnoreCase("default")) {
            if (args[0] == "" || args.length != 1) {
                sender.sendMessage(tacc('&', "&cInvalid arguments"));
                return true;
            }
            Player p = Bukkit.getPlayer(args[0]);
            SCBPlayer scbPlayer = playerManager.getPlayer(p);
            if (perms.has(p, "ascb.vip")) {
                perms.playerRemove(p, "ascb.vip");
            }
            if (perms.has(p, "ascb.gm")) {
                perms.playerRemove(p, "ascb.gm");
            }
            if (p.isOp()) {
                sender.sendMessage(tacc('&', "&cPlayer is op"));
            }
            scbPlayer.recalculate();
            sender.sendMessage(tacc('&', "Defaulted player (Suc: " + (scbPlayer.getRank() == Ranks.DEFAULT) + ")"));
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

        if(cmd.equalsIgnoreCase("class")) {
            if(!(sender instanceof Player))
                return true;
            if(args.length != 1) {
                sender.sendMessage(tacc('&', "&cInvalid arguments"));
                return true;
            }
            String className = "Classes." + args[0].toUpperCase();
            SCBPlayer player = playerManager.getPlayer((Player) sender);
            switch(className) {
                case "Classes.RANDOM":
                    player.setPlayerClass(new ClassRandom());
                    break;
                case "Classes.ZOMBIE":
                    player.setPlayerClass(new ClassZombie());
                    break;
                case "Classes.SKELETON":
                    player.setPlayerClass(new ClassSkeleton());
                    break;
            }
            sender.sendMessage(tacc('&', "&aSet class to &2" + className));
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            final Player player = (Player) e.getEntity();
            final SCBPlayer scbPlayer = playerManager.getPlayer(player);
            if (player.getHealth() - e.getDamage() <= 0) {
                e.setCancelled(true);
                Arena.onPlayerDeath(scbPlayer, null, false);
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    @EventHandler
    public void onPlayerKilledByPlayer(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            final SCBPlayer player = playerManager.getPlayer((Player) e.getEntity());
            if(e.getDamager() instanceof Player) {
                SCBPlayer damager = playerManager.getPlayer((Player)e.getDamager());
                if(player.getPlayer().getHealth() - e.getDamage() <= 0) {
                    e.setCancelled(true);
                    Arena.onPlayerDeath(player, damager, true);
                    player.getPlayer().setHealth(20);
                }
            } else {
                if(player.getPlayer().getHealth() - e.getDamage() <= 0) {
                    e.setCancelled(true);
                    Arena.onPlayerDeath(player, null, false);
                    player.getPlayer().setHealth(20);
                }
            }
        }
    }

    @EventHandler
    public void commandPreprocess(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equalsIgnoreCase("safestop") || e.getMessage() == "/safestop") {
            e.setCancelled(true);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&e" + e.getPlayer().getName() + " &astopped the server!"));
            }
            Bukkit.shutdown();
        }
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e instanceof Player) ((Player) e.getEntity()).setFoodLevel(20);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        sendChatMessage(playerManager.getPlayer(e.getPlayer()), e.getMessage());
    }

    public static String tacc(char c, String msg) {
        return ChatColor.translateAlternateColorCodes(c, msg);
    }

    public void sendChatMessage(SCBPlayer p, String msg) {
        String name = p.getPlayer().getName();
        Ranks rank = p.getRank();

        if (!p.isInGame()) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.sendMessage(ChatColor.translateAlternateColorCodes('&', rank.getPrefix() + " " + name + "&7: " + (rank == Ranks.OP || rank == Ranks.GM ? ChatColor.translateAlternateColorCodes('&', msg) : ChatColor.stripColor(msg))));
            }
        }
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public static Main get() {
        return main;
    }
}
