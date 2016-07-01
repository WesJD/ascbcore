package ascb.nivk.core.arena;

import ascb.nivk.core.Main;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TestArena extends Arena {
    private List<SCBPlayer> players = new ArrayList<SCBPlayer>();
    private HashMap<SCBPlayer, Integer> lives = new HashMap<SCBPlayer, Integer>();
    private boolean isInProgress = false;

    private List<Location> spawnpoints = new ArrayList<Location>();
    private Location lobbyLocation;

    private Random random;

    @Override
    public HashMap<SCBPlayer, Integer> getLives() {
        return lives;
    }

    private Main main;

    public TestArena(Main main) {
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-1.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-3.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-5.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-7.5,174,311.5));
        lobbyLocation = new Location(Bukkit.getWorld("old_lobby"), -4.5,174,316.5);
        random = new Random();
        this.main = main;
    }

    @Override
    public String getName() {
        return "TestArena";
    }

    @Override
    public List<SCBPlayer> getPlayers() {
        return players;
    }

    @Override
    public List<Location> getSpawnpoints() {
        return spawnpoints;
    }

    @Override
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    @Override
    public boolean isInProgress() { return isInProgress; }

    @Override
    public void setInProgress(boolean b) { isInProgress = b;}

}
