package ascb.nivk.core.commands;

import ascb.nivk.core.Main;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            final Player player = (Player) sender;
            final SCBPlayer scbPlayer = Main.get().getPlayerManager().getPlayer(player);
            if(scbPlayer.getCurrentArena() != null) {
                if(args.length > 0) {
                    Main.get().getClassManager().getAll().forEach(scbClass -> {
                        if(scbClass.getName().equalsIgnoreCase(args[0])) {
                            scbPlayer.setCurrentClass(scbClass);
                            scbPlayer.message(ChatColor.GREEN + "You have selected the " +
                                    scbClass.getColor() + scbClass.getName() + ChatColor.GREEN + " class!");
                        }
                    });
                    return true;
                } else scbPlayer.message(ChatColor.RED + "Usage: /class <class>");
            } else scbPlayer.message(ChatColor.RED + "You must be in a game to use this command!");
        } else sender.sendMessage(ChatColor.RED + "Only players can use this command!");
        return false;
    }

}
