package ascb.nivk.core.classes;

import ascb.nivk.core.PlayerClass;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivk on 2016. 07. 01..
 */
public class ClassZombie extends PlayerClass {

    @Override
    public float getJumpPower() {
        return 1.2f;
    }

    @Override
    public String getName() {
        return "Classes.ZOMBIE";
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
        ItemMeta meta = shovel.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
        shovel.setItemMeta(meta);
        items.add(shovel);
        return items;
    }

    @Override
    public ItemStack getHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setColor(Color.GREEN);
        helmet.setItemMeta(meta);
        return helmet;
    }

    @Override
    public ItemStack getChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(Color.GREEN);
        chestplate.setItemMeta(meta);
        return chestplate;
    }

    @Override
    public ItemStack getLeggings() {
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) leggings.getItemMeta();
        meta.setColor(Color.GREEN);
        leggings.setItemMeta(meta);
        return leggings;
    }

    @Override
    public ItemStack getBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta = (LeatherArmorMeta) boots.getItemMeta();
        meta.setColor(Color.GREEN);
        boots.setItemMeta(meta);
        return boots;
    }

    @Override
    public List<PotionEffect> potionEffects() {
        return new ArrayList<PotionEffect>();
    }
}
