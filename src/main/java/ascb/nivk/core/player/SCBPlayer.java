/*
 * MIT License
 *
 * Copyright (c) 2016 Wesley Smith
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ascb.nivk.core.player;

import java.util.UUID;

import ascb.nivk.core.Main;
import ascb.nivk.core.PlayerClass;
import ascb.nivk.core.Ranks;
import ascb.nivk.core.arena.Arena;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.ChatColor;
import ascb.nivk.core.classes.ClassRandom;
import org.bukkit.entity.Player;

public class SCBPlayer {

	private UUID uuid;
	private PlayerClass playerClass;
	private int lives = 5;
	private boolean inGame = false;
	public Arena currentArena = null;
	
	private Ranks rank = Ranks.DEFAULT;
	private Player player;

	private Main main;
	
	public SCBPlayer(UUID id, Player p, Main main) {
		this.uuid = id;
		this.playerClass = new ClassRandom();
		this.lives = 4;
		if(p.hasPermission("ascb.vip")) {
			this.rank = Ranks.VIP;
		} else if(p.hasPermission("ascb.gm")) {
			this.rank = Ranks.GM;
		} else if(p.isOp()) {
			this.rank = Ranks.OP;
		} else {
			this.rank = Ranks.DEFAULT;
		}
		this.player = p;
		this.main = main;
	}

	public void recalculate() {
		Player p = getPlayer();
		this.rank = Ranks.DEFAULT;
		if(p.hasPermission("ascb.vip")) {
			this.rank = Ranks.VIP;
		}
		if(p.hasPermission("ascb.gm")) {
			this.rank = Ranks.GM;
		}
		if(p.isOp()) {
			this.rank = Ranks.OP;
		}
		NametagEdit.getApi().setPrefix(p, ChatColor.translateAlternateColorCodes('&', getRank().getPrefix() + " "));
	}

	public Arena getArena() {
		return this.currentArena;
	}

	public Ranks getRank() {
		return rank;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass) {
		this.playerClass = playerClass;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public UUID getUuid() {
		return uuid;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public Player getPlayer() {
		return player;
	}
}