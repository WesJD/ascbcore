package ascb.nivk.core.commands;

import ascb.nivk.core.Main;
import ascb.nivk.core.player.Rank;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveRankCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            final Player player = (Player) sender;
            final SCBPlayer scbPlayer = Main.get().getPlayerManager().getPlayer(player);
            if(scbPlayer.hasRank(Rank.OP)) {
                if(args.length > 1) {
                    final SCBPlayer target = Main.get().getPlayerManager().getPlayer(Bukkit.getPlayer(args[0]));
                    if(target != null) {
                        Rank rank;
                        try {
                            rank = Rank.valueOf(args[1].toUpperCase());
                        } catch (Exception ex) {
                            scbPlayer.message(ChatColor.RED + "Invalid rank name.");
                            return false;
                        }
                        target.setRank(rank);
                        scbPlayer.message(ChatColor.GREEN + "Success!");
                        return true;
                    } else scbPlayer.message(ChatColor.RED + "\"" + args[0] + "\" isn't online!");
                } else scbPlayer.message(ChatColor.RED + "Usage: /giverank <player> <rank>");
            } else scbPlayer.message(ChatColor.RED + "Only OPs can use this command!");
        } else sender.sendMessage(ChatColor.RED + "Only players can use this commmand!");
        return false;
    }

}
