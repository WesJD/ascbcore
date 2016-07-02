package ascb.nivk.core.arena.impl;

import ascb.nivk.core.Main;
import ascb.nivk.core.arena.AbstractArena;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class TestArena extends AbstractArena {
    private List<Location> spawnpoints = new ArrayList<Location>();
    private Location lobbyLocation;

    public TestArena() {
        spawnpoints.add(new Location(Bukkit.getWorld(Main.ARENA_WORLD),-1.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld(Main.ARENA_WORLD),-3.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld(Main.ARENA_WORLD),-5.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld(Main.ARENA_WORLD),-7.5,174,311.5));
        lobbyLocation = new Location(Bukkit.getWorld(Main.ARENA_WORLD), -4.5,174,316.5);
    }

    @Override
    public String getName() {
        return "TestArena";
    }

    @Override
    public List<Location> getSpawnpoints() {
        return spawnpoints;
    }

    @Override
    public Location getLobbyLocation() {
        return lobbyLocation;
    }

}
