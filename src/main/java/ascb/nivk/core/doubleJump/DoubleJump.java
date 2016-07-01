package ascb.nivk.core.doubleJump;

import java.util.HashMap;
import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import ascb.nivk.core.Main;
import ascb.nivk.core.player.SCBPlayer;

public class DoubleJump implements Listener {
	private final long COOLDOWN_TIME = 1200;

	private final Main main;
	private final Map<Player, Long> disallowed = new HashMap<>();

	public DoubleJump(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onFlightToggle(PlayerToggleFlightEvent e) {
		final Player player = e.getPlayer();
		final SCBPlayer scbPlayer = Main.get().getPlayerManager().getPlayer(player);
		if(player.getGameMode() == GameMode.CREATIVE) return;

		if(disallowed.containsKey(player)) {
			final long startTime = disallowed.get(player);
			if(!((System.currentTimeMillis() - startTime) >= COOLDOWN_TIME)) return;
		}

		e.setCancelled(true);

		player.setAllowFlight(true);
		player.setFlying(true);
		player.setVelocity(new Vector(0, (scbPlayer.isInGame() ? scbPlayer.getPlayerClass().getJumpPower() : 1.6f), 0));
		player.getWorld().playEffect(player.getLocation(), Effect.PARTICLE_SMOKE, 0);

		disallowed.put(player, System.currentTimeMillis());
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location loc = p.getLocation();
        if(disallowed.containsKey(p) && (System.currentTimeMillis() - disallowed.get(p)) >= COOLDOWN_TIME && loc.subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
            disallowed.remove(p);
        }
		if (!disallowed.containsKey(p)) {
			if (p.getGameMode() != GameMode.CREATIVE && loc.subtract(0, 1, 0).getBlock().getType() != Material.AIR
					&& !p.isFlying()) {
				p.setAllowFlight(true);
			}
		} else {
            p.setFlying(false);
            p.setAllowFlight(false);
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
