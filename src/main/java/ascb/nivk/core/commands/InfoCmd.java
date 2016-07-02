package ascb.nivk.core.commands;

import ascb.nivk.core.Main;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InfoCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            final SCBPlayer player = Main.get().getPlayerManager().getPlayer((Player) sender);
            player.message(ChatColor.YELLOW + "Your info:");
            player.message(ChatColor.RED + "Current Class: " +
                    (player.getCurrentClass() != null ? player.getCurrentClass().getName() : "None"));
            player.message(ChatColor.RED + "Ingame: " + (player.getCurrentArena() != null));
            player.message(ChatColor.RED + "Rank: " + player.getRank().getName());
            return true;
        } else sender.sendMessage(ChatColor.RED + "Only players can use this command!");
        return false;
    }

}
