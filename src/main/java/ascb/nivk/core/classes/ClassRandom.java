package ascb.nivk.core.classes;

import ascb.nivk.core.PlayerClass;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ClassRandom extends PlayerClass {
	@Override
	public float getJumpPower() {
		return 1.4f;
	}

	@Override
	public ItemStack getChestplate() {
		return new ItemStack(Material.LEATHER_CHESTPLATE);
	}

	@Override
	public List<PotionEffect> potionEffects() {
		List<PotionEffect> eff = new ArrayList<PotionEffect>();
		eff.add(PotionEffectType.INCREASE_DAMAGE.createEffect(Integer.MAX_VALUE, 2));
		eff.add(PotionEffectType.SPEED.createEffect(Integer.MAX_VALUE, 4));
		return eff;
	}

	@Override
	public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        items.add(new ItemStack(Material.IRON_SWORD));
        items.add(new ItemStack(Material.GOLDEN_APPLE, 5));
		return items;
	}

	@Override
	public ItemStack getHelmet() {
		return new ItemStack(Material.LEATHER_HELMET);
	}

	@Override
	public ItemStack getLeggings() {
		return new ItemStack(Material.LEATHER_LEGGINGS);
	}

	@Override
	public ItemStack getBoots() {
		return new ItemStack(Material.LEATHER_BOOTS);
	}

	@Override
	public String getName() {
		return "Classes.RANDOM";
	}

}
