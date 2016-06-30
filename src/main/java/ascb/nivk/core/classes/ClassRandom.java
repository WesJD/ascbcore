package ascb.nivk.core.classes;

import ascb.nivk.core.PlayerClass;

public class ClassRandom extends PlayerClass {
	@Override
	public float getJumpPower() {
		return 2f;
	}

	@Override
	public String getName() {
		return "Classes.RANDOM";
	}

}
