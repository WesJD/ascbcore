package ascb.nivk.core.arena;

import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private final List<Arena> arenas = new ArrayList<>();

    public ArenaManager() {
        new Reflections("ascb.nivk.core").getSubTypesOf(Arena.class).forEach(clazz -> {
            try {
                arenas.add(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public Arena getArena(Class<? extends Arena> clazz) {
        return arenas.stream().filter(arena -> arena.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public Arena getArena(String name) {
        return arenas.stream().filter(arena -> arena.getName().equals(name)).findFirst().orElse(null);
    }

/*    public void join(SCBPlayer player) {
        Player bukkitPlayer = player.getPlayer();
        if(player.isInGame() && !player.currentArena.getName().equalsIgnoreCase(arena.getName())) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR:&r&c You are already in another game."));
            return;
        }
        if(player.isInGame() && player.currentArena.getName().equalsIgnoreCase(arena.getName())) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR:&r&c You are already in this game"));
            return;
        }
        if(arena.isInProgress()) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR: &r&cArena is in progress"));
            return;
        }
        if(arena.getPlayers().size() == 4) {
            bukkitPlayer.sendMessage(Main.tacc('&', "&c&lERROR: &r&cArena is full"));
            return;
        }
        arena.getPlayers().add(player);
        player.setLives(MAX_LIVES);
        arena.getLives().put(player, player.getLives());
        player.setInGame(true);
        player.currentArena = arena;
        bukkitPlayer.teleport(arena.getLobbyLocation());
        if(arena.getPlayers().size() == 4) {
            int i = 0;
            for(SCBPlayer p : arena.getPlayers()) {
                p.getPlayer().teleport(arena.getSpawnpoints().get(i));
                i++;
                p.getPlayer().sendMessage(Main.tacc('&', "&aThe game is &ostarting"));
                clearInventory(p);
                if(p.getAbstractSCBClass().getName().equalsIgnoreCase("Classes.RANDOM")) {
                    p.setAbstractSCBClass(Main.get().classes.get(random.nextInt(Main.get().classes.size())));
                }
                giveClass(p);
                arena.setInProgress(true);
            }
        }
        arena.getPlayers().forEach(p -> player.getPlayer().sendMessage(Main.tacc('&', "&cThere are/is &e" + arena.getPlayers().size() + "&c player(s) waiting.")));
    }

    public void leave(SCBPlayer player) {
        Arena arena = null;
        for(Arena a : arenas) {
            if(a.getPlayers().contains(player))
                arena = a;
        }
        if(arena == null) {
            Main.get().getServer().getLogger().info("User not in an arena");
            return;
        }
        if(!player.isInGame())
            return;
        checkWinner(arena);
        player.currentArena = null;
        player.setInGame(false);
        player.setLives(5);
        clearInventory(player);
        player.getPlayer().teleport(Main.get().getLobbySpawn());
    }

    public void onPlayerDeath(SCBPlayer player, SCBPlayer attacker, boolean killedByPlayer) {
        if(player.isInGame() && !player.getCurrentArena().isInProgress()) {
            player.getPlayer().sendMessage(Main.tacc('&', "&c&lERROR: &r&cThe match didn\'t even start! How did you die?"));
            player.getPlayer().teleport(player.getCurrentArena().getLobbyLocation());
            return;
        }
        if(!player.isInGame()) {
            player.getPlayer().sendMessage(Main.tacc('&', "&c&lERROR: &r&cDon\'t die in the lobby!"));
            player.getPlayer().teleport(Main.get().getLobbySpawn());
            return;
        }
        player.getPlayer().sendMessage(Main.tacc('&', "&cYou died"));
        player.setLives(player.getLives() - 1);
        if(player.getLives() <= 0) {
            player.getPlayer().sendMessage(Main.tacc('&', "&cYou have been eliminated from the game!"));
            player.getPlayer().teleport(player.getCurrentArena().getLobbyLocation());
            clearInventory(player);
        } else {
            player.getPlayer().teleport(player.getCurrentArena().getSpawnpoints().get(random.nextInt(player.getCurrentArena().getSpawnpoints().size())));
        }
        player.getCurrentArena().getPlayers().forEach(scbPlayer -> {
            if(playerKilled) {
                if(!scbPlayer.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())) {
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&e" + player.getPlayer().getName() + " &cwas killed by &e" + attacker.getPlayer().getName() + "&c!"));
                }
            } else {
                if(!scbPlayer.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())) {
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&e" + player.getPlayer().getName() + " &cdied."));
                }
            }
            if(!scbPlayer.getPlayer().getName().equalsIgnoreCase(player.getPlayer().getName())) {
                if(player.getLives() > 0)
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&cThey have &e" + player.getLives() + " &clives left"));
                else
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&cThey have been eliminated from the match"));
            }
        });
        player.getPlayer().sendMessage(Main.tacc('&', "&cYou have &e" + player.getLives() + "&c lives left."));
        clearInventory(player);
        if(player.getLives() > 0)
            giveClass(player);
        checkWinner(player.getCurrentArena());
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

    private void checkWinner(Arena arena) {
        int ingamePlayers = 0;
        for(SCBPlayer player : arena.getPlayers()) {
            if(player.getLives() >= 1)
                ingamePlayers++;
        }
        if(ingamePlayers == 1) {
            Player winner = arena.getPlayers().get(0).getPlayer();
            winner.sendMessage(Main.tacc('&', "&aYou won!"));
            arena.getPlayers().forEach(scbPlayer -> {
                scbPlayer.getPlayer().teleport(Main.get().getLobbySpawn());
                clearInventory(scbPlayer);
                scbPlayer.currentArena = null;
                scbPlayer.setInGame(false);
                if(!scbPlayer.getPlayer().getName().equalsIgnoreCase(winner.getName())) {
                    scbPlayer.getPlayer().sendMessage(Main.tacc('&', "&a" + winner.getName() + " &cwon the match!"));
                }
            });
            arena.getPlayers().clear();
            arena.getLives().clear();
            arena.setInProgress(false);
        } else if(ingamePlayers == 0) {
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
