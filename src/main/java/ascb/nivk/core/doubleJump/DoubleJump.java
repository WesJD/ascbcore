package ascb.nivk.core.doubleJump;

import java.util.ArrayList;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import ascb.nivk.core.Main;
import ascb.nivk.core.SCBPlayer;

public class DoubleJump implements Listener {
	private Main main;
	public static final ArrayList<Player> cooldown = new ArrayList<Player>();

	public DoubleJump(Main m) {
		this.main = m;
	}

	public class Countdown implements Runnable {
		public Player pl = null;

		public Countdown() {
		}

		public void run() {
			try {
				Thread.sleep(1200);
				DoubleJump.cooldown.removeAll(DoubleJump.cooldown);
				new Thread(this).stop();
			} catch (Exception localException) {
			}
		}
	}

	Countdown d = new Countdown();

	@EventHandler
	public void onFlightToggle(PlayerToggleFlightEvent e) {
		final Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE)
			return;

		ascb.nivk.core.SCBPlayer p2 = null;
		for (SCBPlayer p3 : main.players) {
			if (p3.getUuid().equals(p.getUniqueId())) {
				p2 = p3;
			}
		}
		if (p2 == null) {
			p.kickPlayer("INVALID OPERATION IN PLUGIN. CONTACT AN ADMIN.");
		}

		if (cooldown.contains(p)) {
			;
		} else if (!cooldown.contains(p)) {
			e.setCancelled(true);
			p.setAllowFlight(false);
			p.setFlying(false);

			p.setVelocity(new Vector(0, (p2.isInGame() ? p2.getPlayerClass().getJumpPower() : 1.6f), 0));
			//p.getWorld().playSound(p.getLocation(), Sound.GHAST_FIREBALL, .5f, 1f);
			p.getWorld().playEffect(p.getLocation(), Effect.PARTICLE_SMOKE, 0);

			new Thread(this.d).start();
			cooldown.add(p);
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		if (!cooldown.contains(p)) {
			if (p.getGameMode() != GameMode.CREATIVE && loc.subtract(0, 1, 0).getBlock().getType() != Material.AIR
					&& !p.isFlying()) {
				p.setAllowFlight(true);
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
			}
		}
	}
}
