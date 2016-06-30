package ascb.nivk.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.nametagedit.plugin.NametagEdit;

import ascb.nivk.core.doubleJump.DoubleJump;
import ascb.nivk.core.messages.OnJoin;

public class Main extends JavaPlugin implements Listener {

	private ConsoleCommandSender sender = Bukkit.getConsoleSender();
	public static List<SCBPlayer> players = new ArrayList<SCBPlayer>();
	public static Location lobbySpawn = new Location(Bukkit.getWorld("old_lobby"),0,50,0);

	public FileConfiguration config = getConfig();

	public static Permission perms = null;
	
	BukkitTask announcer;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		SCBPlayer scbp = new SCBPlayer(p.getUniqueId());
		players.add(scbp);
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(20);
		p.setFireTicks(0);
		p.setFoodLevel(20);
		p.teleport(lobbySpawn);
		getServer().getLogger().info("Made new SCB Player! UUID: " + players.get(players.size() - 1).getUuid() + " Array Size: " + players.size());
		scbp.recalculate();
		NametagEdit.getApi().setPrefix(p, ChatColor.translateAlternateColorCodes('&', Main.getPlayerByUUID(p.getUniqueId()).getRank().getPrefix() + " "));
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		for(SCBPlayer p2 : players) {
			if(p.getUniqueId().equals(p2.getUuid())) {
				players.remove(p2);
				getServer().getLogger().info("SCB Player left! UUID: " + p2.getUuid() + " Array Size: " + players.size());
				break;
			}
		}
	}
	
	public static SCBPlayer getPlayerByUUID(UUID id) {
		SCBPlayer p = null;
		for(SCBPlayer p2 : players) {
			if(p2.getUuid().equals(id)) {
				p = p2;
			}
		}
		return p;
	}
	
	public static Player getPlayerFromSCB(SCBPlayer p) {
		return Bukkit.getPlayer(p.getUuid());
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String cmd = command.getName();
		if(cmd.equalsIgnoreCase("ascb")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6The ASCB Project >> &cThis server is running ASCBCore " + this.getDescription().getVersion() + " &6made by&c " + this.getDescription().getAuthors()));
				return true;
			}
		}
		
		if(cmd.equalsIgnoreCase("pinfo")) {
			if(!(sender instanceof Player))
				return false;
			Player p3 = (Player) sender;
			SCBPlayer p = null;
			for(SCBPlayer p2 : players) {
				if(p2.getUuid().equals(p3.getUniqueId())) {
					p = p2;
				}
			}
			if(p == null) {
				sender.sendMessage("INVALID!");
				return true;
			}
			sender.sendMessage("UUID: " + p.getUuid() + " Class: " + p.getPlayerClass().getName() + " Is In Game: " + p.isInGame() + " Rank: " + p.getRank().getName());
		}

		if(cmd.equalsIgnoreCase("giverank")) {
			if(args[0] == null ||args[1] == null) {
				sender.sendMessage(tacc('&',"&cInvalid arguments"));
				return true;
			}
			if(sender instanceof Player) {
				if(getPlayerByUUID(((Player) sender).getUniqueId()).getRank().getLevel() < 2) {
					return false;
				}
			}
			Player p = Bukkit.getPlayer(args[0]);
			perms.playerAdd(p, "ascb."+args[1].toLowerCase());
			getPlayerByUUID(p.getUniqueId()).recalculate();
			sender.sendMessage(tacc('&',"&aRank given"));
			return true;
		}

		if(cmd.equalsIgnoreCase("default")) {
			if(args[0] == null) {
				sender.sendMessage(tacc('&',"&cInvalid arguments"));
				return true;
			}
			Player p = Bukkit.getPlayer(args[0]);
			if(perms.has(p, "ascb.vip")) {
				perms.playerRemove(p, "ascb.vip");
			}
			if(perms.has(p, "ascb.gm")) {
				perms.playerRemove(p, "ascb.gm");
			}
			if(p.isOp()) {
				sender.sendMessage(tacc('&',"&cPlayer is op"));
			}
			getPlayerByUUID(p.getUniqueId()).recalculate();
			sender.sendMessage(tacc('&',"Defaulted player (Suc: " + (getPlayerByUUID(p.getUniqueId()).getRank() == Ranks.DEFAULT) + ")"));
			return true;
		}

		if(cmd.equalsIgnoreCase("recalculate")) {
			if(args[0] == null) {
				sender.sendMessage(tacc('&',"&cInvalid arguments"));
				return true;
			}
			if(sender instanceof Player) {
				if(getPlayerByUUID(((Player) sender).getUniqueId()).getRank().getLevel() < 2) {
					return false;
				}
			}
			Player p = Bukkit.getPlayer(args[0]);
			getPlayerByUUID(p.getUniqueId()).recalculate();
			sender.sendMessage(tacc('&',"&aRecalculated"));
			return true;
		}
		
		if(cmd.equalsIgnoreCase("contributors")) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Contibutors: &e" + this.getDescription().getAuthors()));
			return true;
		}
		
		if(cmd.equalsIgnoreCase("ban")) {
			if(sender instanceof Player) {
				if(getPlayerByUUID(((Player) sender).getUniqueId()).getRank().getLevel() < 2) {
					return false;
				}
			}
			if(args.length != 2) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInvalid arguments! &e/ban {PLAYER} {REASON}"));
				return true;
			}
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aBanned player &e" + args[0] + " &afor the reason: "));
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + args[1]));
			}
			Bukkit.getBanList(Type.NAME).addBan(args[0], args[1], null, sender.getName());
			if(Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&aYou are banned by &e" + sender.getName() + " &afor the reason: &7" + args[1]));
			}
			return true;
		}
		
		if(cmd.equalsIgnoreCase("kick")) {
			if(sender instanceof Player) {
				if(getPlayerByUUID(((Player) sender).getUniqueId()).getRank().getLevel() < 2) {
					return false;
				}
			}
			if(args.length != 2) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aInvalid arguments! &e/kick {PLAYER} {REASON}"));
				return true;
			}
			if(Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).kickPlayer(ChatColor.translateAlternateColorCodes('&', "&aYou are kicked by &e" + sender.getName() + " &afor the reason: &7" + args[1]));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have kicked &e" + args[0] + "&a for the reason:"));
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7" + args[1]));
			} else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPlayer &e" + args[0] + "&a is not Online!"));
			}
			return true;
		}
		return false;
	}
	
	public void sendChatMessage(SCBPlayer p, String msg) {
		String name = getPlayerFromSCB(p).getName();
		Ranks rank = p.getRank();
		
		if(!p.isInGame()) {
			for(Player pl : Bukkit.getOnlinePlayers()) {
				pl.sendMessage(ChatColor.translateAlternateColorCodes('&', rank.getPrefix() + " " + name + "&7: " + (rank == Ranks.OP || rank == Ranks.GM ? ChatColor.translateAlternateColorCodes('&', msg) : ChatColor.stripColor(msg))));
			}
		}
	}	

	@Override
	public void onDisable() {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6The ASCB Project >> &cDisabled"));
	}

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new OnJoin(), this);
		this.getServer().getPluginManager().registerEvents(new DoubleJump(this), this);
		this.getServer().getPluginManager().registerEvents(this, this);
		announcer = new Announcer(this).runTaskTimer(this, 20, 1200);
		config.addDefault("spawn_location", "54x149x331x1x90");
		config.options().copyDefaults(true);
		saveConfig();
		String[] lobbySpawnParts = config.getString("spawn_location").split("x");
		getLogger().info(lobbySpawnParts[0] + " "+ " "+lobbySpawnParts[1]+" "+lobbySpawnParts[2]+" "+lobbySpawnParts[3]);
		double x = Double.parseDouble(lobbySpawnParts[0].replaceAll("x",""));
		double y = Double.parseDouble(lobbySpawnParts[1].replaceAll("x",""));
		double z = Double.parseDouble(lobbySpawnParts[2].replaceAll("x",""));
		lobbySpawn = new Location(Bukkit.getWorld("old_lobby"),x,y,z);
		lobbySpawn.setPitch(Float.parseFloat(lobbySpawnParts[3].replaceAll("x","")));
		lobbySpawn.setYaw(Float.parseFloat(lobbySpawnParts[4].replaceAll("x","")));
		setupPermissions();
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6The ASCB Project >> &aEnabled"));
	}
	
	@EventHandler
	public void commandPreprocess(PlayerCommandPreprocessEvent e) {
		if(e.getMessage().equalsIgnoreCase("safestop") || e.getMessage() == "/safestop") {
			e.setCancelled(true);
			for(Player p : Bukkit.getOnlinePlayers()) {
				p.kickPlayer(ChatColor.translateAlternateColorCodes('&', "&e" + e.getPlayer().getName() + " &astopped the server!"));
			}
			Bukkit.shutdown();
		}
	}
	
	@EventHandler
	public void onPlayerHunger(FoodLevelChangeEvent e) {
		e.setCancelled(true);
		if(e instanceof Player)
			((Player) e.getEntity()).setFoodLevel(20);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		SCBPlayer pl = getPlayerByUUID(p.getUniqueId());
		sendChatMessage(pl, e.getMessage());
		e.setCancelled(true);
	}

	public static String tacc(char c, String msg){return ChatColor.translateAlternateColorCodes(c,msg);}
}
