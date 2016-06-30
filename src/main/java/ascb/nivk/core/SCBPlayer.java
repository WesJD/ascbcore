package ascb.nivk.core;

import java.util.UUID;

import ascb.nivk.core.arena.Arena;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import ascb.nivk.core.classes.ClassRandom;

public class SCBPlayer {
	private UUID uuid;
	private PlayerClass playerClass;
	private int lives = 4;
	private boolean inGame = false;
	private Arena currentArena = null;
	
	private Ranks rank = Ranks.DEFAULT;
	
	public SCBPlayer(UUID id) {
		this.uuid = id;
		this.playerClass = new ClassRandom();
		this.lives = 4;
		Player p = Main.getPlayerFromSCB(this);
		if(p.hasPermission("ascb.vip")) {
			this.rank = Ranks.VIP;
		} else if(p.hasPermission("ascb.gm")) {
			this.rank = Ranks.GM;
		} else if(p.isOp()) {
			this.rank = Ranks.OP;
		} else {
			this.rank = Ranks.DEFAULT;
		}
	}

	public void recalculate() {
		Player p = Main.getPlayerFromSCB(this);
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
		NametagEdit.getApi().setPrefix(p, ChatColor.translateAlternateColorCodes('&', Main.getPlayerByUUID(p.getUniqueId()).getRank().getPrefix() + " "));
	}

	public Arena getArena() {return this.currentArena;}

	public Ranks getRank() {
		return rank;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass) {
		this.playerClass = playerClass;
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
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}
}
