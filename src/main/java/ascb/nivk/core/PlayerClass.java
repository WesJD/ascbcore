package ascb.nivk.core;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;

public abstract class PlayerClass {
	public abstract float getJumpPower();
	public abstract String getName();

	public abstract List<ItemStack> getItems();
	public abstract ItemStack getHelmet();
	public abstract ItemStack getChestplate();
	public abstract ItemStack getLeggings();
	public abstract ItemStack getBoots();

	public abstract List<PotionEffect> potionEffects();
}
