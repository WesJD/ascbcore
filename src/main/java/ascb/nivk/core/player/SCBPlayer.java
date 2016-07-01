package ascb.nivk.core.player;

import java.util.UUID;

import ascb.nivk.core.Main;
import ascb.nivk.core.AbstractSCBClass;
import ascb.nivk.core.Ranks;
import ascb.nivk.core.arena.Arena;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.ChatColor;
import ascb.nivk.core.classes.ClassRandom;
import org.bukkit.entity.Player;

public class SCBPlayer {

	private UUID uuid;
	private AbstractSCBClass abstractSCBClass;
	private int lives = 5;
	private boolean inGame = false;
	public Arena currentArena = null;
	
	private Ranks rank = Ranks.DEFAULT;
	private Player player;

	private Main main;
	
	public SCBPlayer(UUID id, Player p, Main main) {
		this.uuid = id;
		this.abstractSCBClass = new ClassRandom();
		this.lives = 4;
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

	public Arena getArena() {
		return this.currentArena;
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
		return inGame || currentArena != null;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public Player getPlayer() {
		return player;
	}
}
