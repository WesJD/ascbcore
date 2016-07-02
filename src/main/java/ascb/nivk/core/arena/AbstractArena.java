package ascb.nivk.core.arena;

import ascb.nivk.core.Main;
import ascb.nivk.core.classes.AbstractSCBClass;
import ascb.nivk.core.player.SCBPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractArena implements Listener {

    public abstract String getName();
    public abstract List<Location> getSpawnpoints();
    public abstract Location getLobbyLocation();

    public AbstractArena() {
        Bukkit.getPluginManager().registerEvents(this, Main.get());
    }

    private boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public void start() {
        final List<SCBPlayer> players = getPlayers();

        final int[] cur = new int[1];
        getSpawnpoints().forEach(loc -> players.get(cur[0]++).getPlayer().teleport(loc));

        players.forEach(player -> {
            final AbstractSCBClass scbClass = player.getCurrentClass();
            if(scbClass != null) scbClass.apply(player);
            else {

            }
        });

        //TODO - Start the game
    }

    public void join(SCBPlayer scbPlayer) {
        final Player player = scbPlayer.getPlayer();
        player.teleport(getLobbyLocation());

        scbPlayer.setCurrentArena(this);
        scbPlayer.setLives(5);
        scbPlayer.message(ChatColor.GREEN + "You have joined " + ChatColor.YELLOW + getName() +
                ChatColor.GREEN + ". Choose your class by clicking on one of the signs!");
        if(getPlayers().size() == 4) start();
    }

    public void leave(SCBPlayer player) {
        //TODO - Handle leave
    }

    public List<SCBPlayer> getPlayers() {
        return Main.get().getPlayerManager().getPlayers().stream()
                .filter(scbPlayer -> scbPlayer.getCurrentArena().equals(this))
                .collect(Collectors.toList());
    }

    public enum State {

        WAITING, STARTING, PLAYING

    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            final Player player = (Player) e.getEntity();
            final SCBPlayer scbPlayer = Main.get().getPlayerManager().getPlayer(player);
            if (player.getHealth() - e.getDamage() <= 0) {
                e.setCancelled(true);
                //TODO - Handle player death
                //AbstractArena.onPlayerDeath(scbPlayer, null, false);
                player.setHealth(player.getMaxHealth());
            }
        }
    }
    @EventHandler
    public void onPlayerKilledByPlayer(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player) {
            final SCBPlayer player = Main.get().getPlayerManager().getPlayer((Player) e.getEntity());
            if(e.getDamager() instanceof Player) {
                SCBPlayer damager = Main.get().getPlayerManager().getPlayer((Player)e.getDamager());
                if(player.getPlayer().getHealth() - e.getDamage() <= 0) {
                    e.setCancelled(true);
                    //TODO - Handle player death
                    //AbstractArena.onPlayerDeath(scbPlayer, damager, true);
                    player.getPlayer().setHealth(20);
                }
            } else {
                if(player.getPlayer().getHealth() - e.getDamage() <= 0) {
                    e.setCancelled(true);
                    //TODO - Handle player death
                    //AbstractArena.onPlayerDeath(scbPlayer, damager, false);
                    player.getPlayer().setHealth(20);
                }
            }
        }
    }

/*    public static final int MAX_LIVES = 5;

    private static Random random = new Random();

    public static void onPlayerJoin(SCBPlayer player, AbstractArena arena) {
        Player bukkitPlayer = player.getPlayer();
        if (player.isInGame() && !player.currentArena.getName().equalsIgnoreCase(arena.getName())) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR:&r&c You are already in another game."));
            return;
        }
        if (player.isInGame() && player.currentArena.getName().equalsIgnoreCase(arena.getName())) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR:&r&c You are already in this game"));
            return;
        }
        if (arena.isInProgress()) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR: &r&cArena is in progress"));
            return;
        }
        if (arena.getPlayers().size() == 4) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR: &r&cArena is full"));
            return;
        }
        arena.getPlayers().add(player);
        player.setLives(MAX_LIVES);
        arena.getLives().put(player, player.getLives());
        player.setInGame(true);
        player.currentArena = arena;
        bukkitPlayer.teleport(arena.getLobbyLocation());
        if (arena.getPlayers().size() == 4) {
            int i = 0;
            for (SCBPlayer p : arena.getPlayers()) {
                p.getPlayer().teleport(arena.getSpawnpoints().get(i));
                i++;
                p.getPlayer().sendMessage(Main.tacc('&', "&aThe game is &ostarting"));
                clearInventory(p);
                if (p.getCurrentClass().getName().equalsIgnoreCase("Classes.RANDOM")) {
                    p.setCurrentClass(Main.get().classes.get(random.nextInt(Main.get().classes.size())));
                }
                giveClass(p);
                arena.setInProgress(true);
            }
        }
        arena.getPlayers().forEach(p -> player.getPlayer().sendMessage(Main.tacc('&', "&cThere are/is &e" + arena.getPlayers().size() + "&c player(s) waiting.")));
    }

    public static void onPlayerDeath(SCBPlayer player, SCBPlayer attacker, boolean playerKilled) {
        if (player.isInGame() && !player.getCurrentArena().isInProgress()) {
            player.getPlayer().sendMessage(Main.tacc('&', "&c&lERROR: &r&cThe match didn\'t even start! How did you die?"));
            player.getPlayer().teleport(player.getCurrentArena().getLobbyLocation());
            return;
        }
        if (!player.isInGame()) {
            player.getPlayer().sendMessage(Main.tacc('&', "&c&lERROR: &r&cDon\'t die in the lobby!"));
            player.getPlayer().teleport(Main.get().getLobbySpawn());
            return;
        }
        player.getPlayer().sendMessage(Main.tacc('&', "&cYou died"));
        player.setLives(player.getLives() - 1);
        if (player.getLives() <= 0) {
            player.getPlayer().sendMessage(Main.tacc('&', "&cYou have been eliminated from the game!"));
            player.getPlayer().teleport(player.getCurrentArena().getLobbyLocation());
            clearInventory(player);
        } else {
            player.getPlayer().teleport(player.getCurrentArena().getSpawnpoints().get(random.nextInt(player.getCurrentArena().getSpawnpoints().size())));
        }
        player.getCurrentArena().getPlayers().forEach(scbPlayer -> {
            if (playerKilled) {
                if (!scbPlayer.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())) {
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&e" + player.getPlayer().getName() + " &cwas killed by &e" + attacker.getPlayer().getName() + "&c!"));
                }
            } else {
                if (!scbPlayer.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())) {
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&e" + player.getPlayer().getName() + " &cdied."));
                }
            }
            if (!scbPlayer.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())) {
                if (player.getLives() > 0)
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&cThey have &e" + player.getLives() + " &clives left"));
                else
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&cThey have been eliminated from the match"));
            }
        });
        player.getPlayer().sendMessage(Main.tacc('&', "&cYou have &e" + player.getLives() + "&c lives left."));
        clearInventory(player);
        if (player.getLives() > 0)
            giveClass(player);
        checkWinner(player.getCurrentArena());
    }

    public static void onPlayerLeave(SCBPlayer scbPlayer) {
        ArenaManager.get().leave(scbPlayer);
    }

    private static void clearInventory(SCBPlayer player) {
        player.getPlayer().getInventory().clear();
        player.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
        player.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
        player.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
        player.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
        for (PotionEffect eff : player.getPlayer().getActivePotionEffects())
            player.getPlayer().removePotionEffect(eff.getType());
    }

    public static void giveClass(SCBPlayer player) {
        player.getCurrentClass().apply(player);
    }

    private static void checkWinner(AbstractArena arena) {
        int ingamePlayers = 0;
        for (SCBPlayer player : arena.getPlayers()) {
            if (player.getLives() >= 1)
                ingamePlayers++;
        }
        if (ingamePlayers == 1) {
            Player winner = arena.getPlayers().get(0).getPlayer();
            winner.sendMessage(Main.tacc('&', "&aYou won!"));
            arena.getPlayers().forEach(scbPlayer -> {
                scbPlayer.getPlayer().teleport(Main.get().getLobbySpawn());
                clearInventory(scbPlayer);
                scbPlayer.currentArena = null;
                scbPlayer.setInGame(false);
                if (!scbPlayer.getPlayer().getName().equalsIgnoreCase(winner.getName())) {
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&a" + winner.getName() + " &cwon the match!"));
                }
            });
            arena.getPlayers().clear();
            arena.getLives().clear();
            arena.setInProgress(false);
        } else if (ingamePlayers == 0) {
            arena.getPlayers().forEach(scbPlayer -> {
                Player bukkitPlayer = scbPlayer.getPlayer();
                bukkitPlayer.teleport(Main.get().getLobbySpawn());
                bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR: &r&cNo winner."));
                scbPlayer.currentArena = null;
                scbPlayer.setInGame(false);
            });
            arena.getPlayers().clear();
            arena.getLives().clear();
            arena.setInProgress(false);
        }
    }*/
}
