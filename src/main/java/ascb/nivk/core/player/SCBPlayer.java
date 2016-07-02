package ascb.nivk.core.player;

import java.util.UUID;

import ascb.nivk.core.Main;
import ascb.nivk.core.arena.AbstractArena;
import ascb.nivk.core.classes.AbstractSCBClass;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.entity.Player;

public class SCBPlayer {

	private final Player player;
	private final UUID uuid;
	private Rank rank;

	private int lives;
	private AbstractArena currentArena;
	private AbstractSCBClass abstractSCBClass;
	
	public SCBPlayer(UUID uuid, Player player) {
		this.uuid = uuid;
		this.player = player;

		if(player.isOp()) setRank(Rank.OP);
		else if(player.hasPermission("ascb.gm")) setRank(Rank.GM);
		else if(player.hasPermission("ascb.vip")) setRank(Rank.VIP);
		else setRank(Rank.DEFAULT);
	}

	public AbstractArena getCurrentArena() {
		return this.currentArena;
	}

	public void setCurrentArena(AbstractArena arena) {
		currentArena = arena;
	}

	public void setRank(Rank rank) {
		Main.get().getPermissions().playerRemove(player, "ascb." + rank.toString().toLowerCase());
		this.rank = rank;
		Main.get().getPermissions().playerAdd(player, "ascb." + rank.toString().toLowerCase());
		NametagEdit.getApi().setPrefix(player, rank.getTag());
	}

	public boolean hasRank(Rank rank) {
		return (this.rank.compareTo(rank) >= 0);
	}

	public Rank getRank() {
		return rank;
	}

	public AbstractSCBClass getCurrentClass() {
		return abstractSCBClass;
	}

	public void setCurrentClass(AbstractSCBClass abstractSCBClass) {
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
