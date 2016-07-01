package ascb.nivk.core.classes;

import ascb.nivk.core.AbstractSCBClass;
import ascb.nivk.core.Main;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nivk on 2016. 07. 01..
 */
public class ClassSkeleton extends AbstractSCBClass {
    @Override
    public float getJumpPower() {
        return 1.2f;
    }

    @Override
    public String getName() {
        return "Classes.SKELETON";
    }

    @Override
    public String getDisplayname() {
        return Main.tacc('&', "&7[&fSkeleton&f]&7");
    }

    @Override
    public int getLevel() {
        return AbstractSCBClass.DEFAULT;
    }

    @Override
    public void apply(SCBPlayer player) {
        List<ItemStack> items = new ArrayList<>();
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemStack bone = new ItemStack(Material.BONE, 1);
        bone.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        ItemMeta boneMeta = bone.getItemMeta();
        boneMeta.setDisplayName("Skeleton Bone");
        bone.setItemMeta(boneMeta);
        items.add(bow);
        items.add(bone);
        items.add(new ItemStack(Material.ARROW, 1));
        ItemStack helmet = new ItemStack(Material.SKULL_ITEM, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta)chestplate.getItemMeta();
        meta.setColor(Color.GRAY);
        chestplate.setItemMeta(meta);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta meta2 = (LeatherArmorMeta)leggings.getItemMeta();
        meta2.setColor(Color.GRAY);
        leggings.setItemMeta(meta);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta meta3 = (LeatherArmorMeta)boots.getItemMeta();
        meta3.setColor(Color.GRAY);
        meta3.addEnchant(Enchantment.PROTECTION_FALL, 10, true);
        meta3.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        boots.setItemMeta(meta);

        Player bukkitPlayer = player.getPlayer();
        items.forEach(item -> bukkitPlayer.getInventory().addItem(item));
        bukkitPlayer.getInventory().setHelmet(helmet);
        bukkitPlayer.getInventory().setChestplate(chestplate);
        bukkitPlayer.getInventory().setLeggings(leggings);
        bukkitPlayer.getInventory().setBoots(boots);
    }
}
