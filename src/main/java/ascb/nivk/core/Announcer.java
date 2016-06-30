package ascb.nivk.core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;

public class Announcer extends BukkitRunnable {

	public Announcer(Main m) {
		Main main = m;
	}
	
	@Override
	public void run() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&dExecute Double Jumps by tapping the space bar twice!"));
		}
	}
}
