package ascb.nivk.core.player;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<Player, SCBPlayer> players = new HashMap<>();

    public SCBPlayer getPlayer(UUID uuid) {
        SCBPlayer ret = null;
        for(SCBPlayer player : players.values()) if(player.getUuid().equals(uuid)) {
            ret = player;
            break;
        }
        return ret;
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

}
