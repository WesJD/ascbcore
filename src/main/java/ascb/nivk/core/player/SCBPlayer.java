package ascb.nivk.core.player;

import java.util.UUID;

import ascb.nivk.core.Main;
import ascb.nivk.core.classes.AbstractSCBClass;
import ascb.nivk.core.Ranks;
import ascb.nivk.core.arena.Arena;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SCBPlayer {

	private final UUID uuid;
	private AbstractSCBClass abstractSCBClass;
	private int lives = 5;
	public Arena currentArena = null;
	
	private Ranks rank = Ranks.DEFAULT;
	private Player player;

	private Main main;
	
	public SCBPlayer(UUID id, Player p, Main main) {
		this.uuid = id;
		if(p.hasPermission("ascb.vip")) {
			this.rank = Ranks.VIP;
		}
		if(p.hasPermission("ascb.gm")) {
			this.rank = Ranks.GM;
		}
		if(p.isOp()) {
			this.rank = Ranks.OP;
		}
		this.player = p;
		this.main = main;
	}

	public void recalculate() {
		Player p = getPlayer();
		this.rank = Ranks.DEFAULT;
		if(p.hasPermission("ascb.vip")) {
			this.rank = Ranks.VIP;
		}
		if(p.hasPermission("ascb.gm")) {
			this.rank = Ranks.GM;
		}
		if(p.isOp()) {
			this.rank = Ranks.OP;
		}
		NametagEdit.getApi().setPrefix(p, ChatColor.translateAlternateColorCodes('&', getRank().getPrefix() + " "));
	}

	public Arena getCurrentArena() {
		return this.currentArena;
	}

	public void setCurrentArena(Arena arena) {
		currentArena = arena;
	}

	public Ranks getRank() {
		return rank;
	}

	public AbstractSCBClass getAbstractSCBClass() {
		return abstractSCBClass;
	}

	public void setAbstractSCBClass(AbstractSCBClass abstractSCBClass) {
		this.abstractSCBClass = abstractSCBClass;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isInGame() {
		return currentArena != null;
	}

	public Player getPlayer() {
		return player;
	}

	public void message(String message) {
		player.sendMessage(message);
	}

}
