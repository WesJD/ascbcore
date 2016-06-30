package ascb.nivk.core;

public enum Ranks {
	DEFAULT("DEFAULT", "&7", 0), VIP("VIP", "&f[&6VIP&f]&7", 1), GM("GM", "&f[&bGM&f]&f", 2), OP("OP", "&f[&cOp&f]&f", 3);
	
	private final String name;
	private final String prefix;
	private final int level;

	Ranks(String name, String prefix, int level) {
		this.name = name;
		this.prefix = prefix;
		this.level = level;
	}
	
	public String getName() {
		return name;
	}
	public String getPrefix() {
		return prefix;
	}
	public int getLevel() { return level; }
}
