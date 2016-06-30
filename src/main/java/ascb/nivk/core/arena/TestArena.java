package ascb.nivk.core.arena;

import ascb.nivk.core.Main;
import ascb.nivk.core.PlayerClass;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TestArena extends Arena {
    private List<SCBPlayer> players = new ArrayList<SCBPlayer>();
    private HashMap<SCBPlayer, Integer> lives = new HashMap<SCBPlayer, Integer>();
    private List<SCBPlayer> ingamePlayers = new ArrayList<SCBPlayer>();

    private List<Location> spawnpoints = new ArrayList<Location>();
    private Location lobbyLocation;

    private Random random;

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
    public void onPlayerJoin(SCBPlayer player) {
        if(players.contains(player)) {
            player.getPlayer().teleport(lobbyLocation);
            player.getPlayer().sendMessage("you already in");
            return;
        }
        players.add(player);
        player.setLives(5);
        lives.put(player, player.getLives());
        ingamePlayers.add(player);
        player.setInGame(true);
        player.getPlayer().teleport(getLobbyLocation());
        player.currentArena = this;
        if(players.size() == 4) {
            int i = 0;
            for(SCBPlayer p : players) {
                player.getPlayer().teleport(spawnpoints.get(i));
                i++;
                player.getPlayer().sendMessage("start");
            }
        }
        giveClass(player);
    }

    @Override
    public void onPlayerLeave(SCBPlayer player) {
        players.remove(player);
        lives.remove(player);
        player.setLives(5);
        player.getPlayer().teleport(Main.get().getLobbySpawn());
        checkWinner();
        player.setInGame(false);
        clearInventory(player);
    }

    @Override
    public void onPlayerDeath(SCBPlayer player, SCBPlayer attacker) {
        player.getPlayer().sendMessage("you ded " + ingamePlayers.toString());
        player.setLives(player.getLives() - 1);
        if(player.getLives() <= 0) {
            player.getPlayer().sendMessage("ur out");
            player.getPlayer().teleport(lobbyLocation);
            ingamePlayers.remove(player);
            clearInventory(player);
            return;
        }
        player.getPlayer().teleport(spawnpoints.get(random.nextInt(spawnpoints.size())));
        for(SCBPlayer p : players) {
            if(attacker != null) {
                p.getPlayer().sendMessage(Bukkit.getPlayer(player.getUuid()).getName() + " ded by " + Bukkit.getPlayer(attacker.getUuid()).getName());
            } else {
                p.getPlayer().sendMessage("he ded from null");
            }
            p.getPlayer().sendMessage("he has " + player.getLives() + " lives left!!");
        }
        clearInventory(player);
        giveClass(player);
        checkWinner();
    }

    public void checkWinner() {
        if(ingamePlayers.size() == 1) {
            Player winner = ingamePlayers.get(0).getPlayer();
            winner.sendMessage("you win");
            for(SCBPlayer p : players) {
                Player p2 = p.getPlayer();
                p2.teleport(Main.get().getLobbySpawn());
                lives.clear();
                ingamePlayers.clear();
                clearInventory(p);
            }
            players.clear();
        }
    }

    private void clearInventory(SCBPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
        for(PotionEffect eff : player.getPlayer().getActivePotionEffects())
            player.getPlayer().removePotionEffect(eff.getType());
    }

    public void giveClass(SCBPlayer player) {
        PlayerClass playerClass = player.getPlayerClass();
        List<ItemStack> items = playerClass.getItems();
        for(ItemStack item : items) {
            Player p = player.getPlayer();
            p.getInventory().addItem(item);
        }
        List<PotionEffect> effects = playerClass.potionEffects();
        Player p = player.getPlayer();
        p.addPotionEffects(effects);
        p.getInventory().setHelmet(playerClass.getHelmet());
        p.getInventory().setChestplate(playerClass.getChestplate());
        p.getInventory().setLeggings(playerClass.getLeggings());
        p.getInventory().setBoots(playerClass.getBoots());
    }
}
