package ascb.nivk.core.player;

import org.bukkit.ChatColor;

public enum Rank {

	DEFAULT(null, ChatColor.GRAY),
	VIP("VIP", ChatColor.GOLD), //gold back then
	GM("GM", ChatColor.RED), //red back then
	OP("Op", ChatColor.RED); //^^
	
	private final String name;
	private final ChatColor chatColor;

	Rank(String name, ChatColor chatColor) {
		this.name = name;
		this.chatColor = chatColor;
	}
	
	public String getName() {
		return name;
	}

	public ChatColor getColor() {
		return chatColor;
	}

	public String getTag() {
		return (name != null ? ChatColor.WHITE + "[" + chatColor + name + ChatColor.WHITE + "]" : "");
	}

}
