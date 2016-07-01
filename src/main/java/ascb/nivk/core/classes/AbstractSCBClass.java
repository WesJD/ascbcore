package ascb.nivk.core.classes;

import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.ChatColor;

public abstract class AbstractSCBClass {

	public abstract String getName();
	public abstract ChatColor getColor();
	public abstract float getJumpPower();
	public abstract void apply(SCBPlayer player);
	public abstract ClassType getType();

	public String getDisplayName() {
		return getColor() + getName();
	}

	public enum ClassType {
		DEFAULT, VIP
	}

}
