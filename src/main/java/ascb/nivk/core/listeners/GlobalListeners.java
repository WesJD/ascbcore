package ascb.nivk.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class GlobalListeners implements Listener {

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e instanceof Player) ((Player) e.getEntity()).setFoodLevel(20);
    }

}
