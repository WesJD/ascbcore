package ascb.nivk.core.arena;

import ascb.nivk.core.SCBPlayer;
import org.bukkit.Location;

import java.util.List;

public abstract class Arena {
    public abstract String getName();
    public abstract List<SCBPlayer> getPlayers();
    public abstract List<Location> getSpawnpoints();
    public abstract Location getLobbyLocation();

    public abstract void onPlayerJoin(SCBPlayer player);
    public abstract void onPlayerLeave(SCBPlayer player);
    public abstract void onPlayerDeath(SCBPlayer player, SCBPlayer attacker);
}
