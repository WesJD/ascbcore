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
public class ClassSkeleton extends PlayerClass {
    @Override
    public float getJumpPower() {
        return 1.2f;
    }

    @Override
    public String getName() {
        return "Classes.SKELETON";
    }

    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemStack bone = new ItemStack(Material.BONE, 1);
        bone.addEnchantment(Enchantment.KNOCKBACK, 1);
        ItemMeta boneMeta = bone.getItemMeta();
        boneMeta.setDisplayName("Skeleton Bone");
        bone.setItemMeta(boneMeta);
        items.add(bow);
        items.add(bone);
        return items;
    }

    @Override
    public ItemStack getHelmet() {
        return new ItemStack(Material.SKULL, 1);
    }

    @Override
    public ItemStack getChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta)chestplate.getItemMeta();
        meta.setColor(Color.GRAY);
        chestplate.setItemMeta(meta);
        return chestplate;
    }

    @Override
    public ItemStack getLeggings() {
        ItemStack leggings = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta)leggings.getItemMeta();
        meta.setColor(Color.GRAY);
        leggings.setItemMeta(meta);
        return leggings;
    }

    @Override
    public ItemStack getBoots() {
        ItemStack boots = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta)boots.getItemMeta();
        meta.setColor(Color.GRAY);
        meta.addEnchant(Enchantment.PROTECTION_FALL, 10, true);
        meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        boots.setItemMeta(meta);
        return boots;
    }

    @Override
    public List<PotionEffect> potionEffects() {
        return new ArrayList<>();
    }
}
