package ascb.nivk.core.player;

import org.bukkit.entity.Player;

import java.util.*;

public class PlayerManager {

    private final Map<Player, SCBPlayer> players = new HashMap<>();

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

}
