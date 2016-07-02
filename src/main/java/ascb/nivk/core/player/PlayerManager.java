package ascb.nivk.core.player;

import ascb.nivk.core.Main;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class PlayerManager {

    private final Map<Player, SCBPlayer> players = new HashMap<>();

    public PlayerManager() {
        Bukkit.getPluginManager().registerEvents(new Listeners(), Main.get());
    }

    public SCBPlayer getPlayer(UUID uuid) {
        return players.values().stream().filter(scbPlayer -> scbPlayer.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public SCBPlayer getPlayer(Player player) {
        return players.get(player);
    }

    public void addPlayer(Player player, SCBPlayer scbPlayer) {
        players.put(player, scbPlayer);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Collection<SCBPlayer> getPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    private class Listeners implements Listener {

        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
            final Player player = e.getPlayer();
            final SCBPlayer scbPlayer = new SCBPlayer(player.getUniqueId(), player);

            addPlayer(player, scbPlayer);

            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20);
            player.setFireTicks(0);
            player.setFoodLevel(20);
            player.teleport(Main.get().getLobbySpawn());
        }

        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent e) {
            final Player player = e.getPlayer();
            final SCBPlayer scbPlayer = getPlayer(player);
            scbPlayer.getCurrentArena().leave(scbPlayer);
            removePlayer(player);
        }

    }

}
