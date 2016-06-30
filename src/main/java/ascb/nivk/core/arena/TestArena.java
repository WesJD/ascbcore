package ascb.nivk.core.arena;

import ascb.nivk.core.Main;
import ascb.nivk.core.SCBPlayer;
import com.comphenix.protocol.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Nivk on 2016. 06. 30..
 */
public class TestArena extends Arena {
    private List<SCBPlayer> players = new ArrayList<SCBPlayer>();
    private HashMap<SCBPlayer, Integer> lives = new HashMap<SCBPlayer, Integer>();
    private List<SCBPlayer> ingamePlayers = new ArrayList<SCBPlayer>();

    private List<Location> spawnpoints = new ArrayList<Location>();
    private Location lobbyLocation;

    private Random random;

    public TestArena() {
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-1.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-3.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-5.5,174,311.5));
        spawnpoints.add(new Location(Bukkit.getWorld("old_lobby"),-7.5,174,311.5));
        lobbyLocation = new Location(Bukkit.getWorld("old_lobby"), -4.5,174,316.5);
        random = new Random();
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
    public void onPlayerJoin(SCBPlayer player) {
        if(players.contains(player)) {
            player.getPlayer().teleport(lobbyLocation);
            player.getPlayer().sendMessage("you already in");
            return;
        }
        players.add(player);
        player.setLives(4);
        lives.put(player, player.getLives());
        ingamePlayers.add(player);
        player.setInGame(true);
        Main.getPlayerFromSCB(player).teleport(getLobbyLocation());
        player.currentArena = this;
        if(players.size() == 4) {
            int i = 0;
            for(SCBPlayer p : players) {
                Main.getPlayerFromSCB(p).teleport(spawnpoints.get(i));
                i++;
                Main.getPlayerFromSCB(p).sendMessage("start");
            }
        }
    }

    @Override
    public void onPlayerLeave(SCBPlayer player) {
        players.remove(player);
        lives.remove(player);
        player.setLives(4);
        Main.getPlayerFromSCB(player).teleport(Main.lobbySpawn);
        checkWinner();
        player.setInGame(false);
    }

    @Override
    public void onPlayerDeath(SCBPlayer player, SCBPlayer attacker) {
        Main.getPlayerFromSCB(player).sendMessage("you ded " + ingamePlayers.toString());
        player.setLives(player.getLives() - 1);
        if(player.getLives() <= 0) {
            Main.getPlayerFromSCB(player).sendMessage("ur out");
            Main.getPlayerFromSCB(player).teleport(lobbyLocation);
            ingamePlayers.remove(player);
            return;
        }
        Main.getPlayerFromSCB(player).teleport(spawnpoints.get(random.nextInt(spawnpoints.size())));
        for(SCBPlayer p : players) {
            if(attacker != null) {
                Main.getPlayerFromSCB(p).sendMessage(Bukkit.getPlayer(player.getUuid()).getName() + " ded by " + Bukkit.getPlayer(attacker.getUuid()).getName());
            } else {
                p.getPlayer().sendMessage("he ded from null");
            }
            Main.getPlayerFromSCB(p).sendMessage("he has " + player.getLives() + " lives left!!");
        }
        checkWinner();
    }

    public void checkWinner() {
        if(ingamePlayers.size() == 1) {
            Player winner = Main.getPlayerFromSCB(ingamePlayers.get(0));
            winner.sendMessage("you win");
            for(SCBPlayer p : players) {
                Player p2 = Main.getPlayerFromSCB(p);
                p2.teleport(Main.lobbySpawn);
                players.remove(p);
                lives.clear();
                ingamePlayers.clear();
            }
        }
    }
}
