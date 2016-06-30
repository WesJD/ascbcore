package ascb.nivk.core.arena;

import ascb.nivk.core.SCBPlayer;
import org.bukkit.Location;

import java.util.List;

/**
 * Created by Nivk on 2016. 06. 30..
 */
public abstract class Arena {
    public abstract String getName();
    public abstract List<SCBPlayer> getPlayers();
    public abstract List<Location> getSpawnpoints();
    public abstract Location getLobbyLocation();
}
