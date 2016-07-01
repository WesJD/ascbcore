package ascb.nivk.core;

import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public abstract class AbstractSCBClass {
	public abstract float getJumpPower();
	public abstract String getName();
	public abstract void apply(SCBPlayer player);

	public abstract String getDisplayname();
	public abstract int getLevel();

	public static final int DEFAULT = 0;
	public static final int VIP = 1;
}
