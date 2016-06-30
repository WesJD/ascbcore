package ascb.nivk.core;

public enum Ranks {

	DEFAULT("DEFAULT", "&7"),
	VIP("VIP", "&f[&6VIP&f]&7"),
	GM("GM", "&f[&bGM&f]&f"),
	OP("OP", "&f[&cOp&f]&f");
	
	private final String name;
	private final String prefix;

	Ranks(String name, String prefix) {
		this.name = name;
		this.prefix = prefix;
	}
	
	public String getName() {
		return name;
	}
	public String getPrefix() {
		return prefix;
	}
	public int getLevel() {
		return ordinal();
	}
}
