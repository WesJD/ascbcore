package ascb.nivk.core.commands;

import ascb.nivk.core.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

public class ASCBCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        final PluginDescriptionFile desc = Main.get().getDescription();
        sender.sendMessage(ChatColor.RED + "This server is running ASCBCore " + desc.getVersion() + " made by " + desc.getAuthors());
        return true;
    }

}
