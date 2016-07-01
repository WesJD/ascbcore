package ascb.nivk.core.arena;

import ascb.nivk.core.Main;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    private static ArenaManager manager = null;
    private TestArena testArena;

    private List<Arena> arenas;

    private ArenaManager() {
        testArena = new TestArena(Main.get());
        arenas = new ArrayList<>();
        arenas.add(testArena);
    }

    public static ArenaManager get() {
        if(manager == null)
            manager = new ArenaManager();
        return manager;
    }

    public Arena getArena(String name) {
        for(Arena arena : arenas) {
            if(arena.getName().equalsIgnoreCase(name))
                return arena;
        }
        return null;
    }
}
