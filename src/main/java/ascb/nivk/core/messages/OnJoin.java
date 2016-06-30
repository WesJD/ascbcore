package ascb.nivk.core.messages;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.ChatColor;

public class OnJoin implements Listener {

	private final String JOIN_MESSAGE = "PLAYER &6joined &cASCB&6!";
	private final String MOTD = "&6Welcome &cPLAYER &6to the ASCB Project! &8&o(This is an early implementation of the ASCB Core Plugin)";

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.setJoinMessage(null);
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(p.getName().equalsIgnoreCase(e.getPlayer().getName()))
				continue;
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',
					JOIN_MESSAGE.replaceAll("PLAYER", e.getPlayer().getName())));
		}
		e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', MOTD.replaceAll("PLAYER", e.getPlayer().getName())));
		Bukkit.getConsoleSender().sendMessage(e.getPlayer().getName() + " joined the ASCB Project!"); 
	}

}
