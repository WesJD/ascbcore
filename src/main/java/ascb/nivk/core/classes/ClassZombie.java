package ascb.nivk.core.classes;

import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassZombie extends AbstractSCBClass {

    @Override
    public String getName() {
        return "Classes.ZOMBIE";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
    }

    @Override
    public float getJumpPower() {
        return 1.2F;
    }

    @Override
    public void apply(SCBPlayer player) {
        List<ItemStack> items = new ArrayList<>();
        ItemStack shovel = new ItemStack(Material.IRON_SPADE, 1);
        ItemStack helmet = new ItemStack(Material.SKULL_ITEM, 1, (short)2);
        ItemMeta meta = shovel.getItemMeta();
        meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
        meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
        shovel.setItemMeta(meta);
        items.add(shovel);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta meta2 = (LeatherArmorMeta) chestplate.getItemMeta();
        meta2.setColor(Color.TEAL);
        chestplate.setItemMeta(meta2);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta meta3 = (LeatherArmorMeta) leggings.getItemMeta();
        meta3.setColor(Color.BLUE);
        leggings.setItemMeta(meta3);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        LeatherArmorMeta meta4 = (LeatherArmorMeta) boots.getItemMeta();
        meta4.setColor(Color.RED);
        meta4.addEnchant(Enchantment.PROTECTION_FALL, 10, true);
        meta4.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
        boots.setItemMeta(meta4);
        Player bukkitPlayer = player.getPlayer();

        items.forEach(item -> bukkitPlayer.getInventory().addItem(item));
        bukkitPlayer.getInventory().setHelmet(helmet);
        bukkitPlayer.getInventory().setChestplate(chestplate);
        bukkitPlayer.getInventory().setLeggings(leggings);
        bukkitPlayer.getInventory().setBoots(boots);
    }

    @Override
    public ClassType getType() {
        return ClassType.DEFAULT;
    }

}
