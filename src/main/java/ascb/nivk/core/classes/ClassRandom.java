package ascb.nivk.core.classes;

import ascb.nivk.core.AbstractSCBClass;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ClassRandom extends AbstractSCBClass {
	@Override
	public float getJumpPower() {
		return 1.4f;
	}

	@Override
	public String getName() {
		return "Classes.RANDOM";
	}

	@Override
	public void apply(SCBPlayer player) {
		;
	}

	@Override
	public String getDisplayname() {
		return "";
	}

	@Override
	public int getLevel() {
		return AbstractSCBClass.DEFAULT;
	}
}
