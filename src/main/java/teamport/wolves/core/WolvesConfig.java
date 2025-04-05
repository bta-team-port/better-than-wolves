package teamport.wolves.core;

import teamport.wolves.BetterThanWolves;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

public class WolvesConfig {
	private static final Toml TOML = new Toml("Better Than Wolves");
	public static TomlConfigHandler cfg;

	static {
		TOML.addCategory("IDs")
			.addEntry("blockIDs", 2048)
			.addEntry("itemIDs", 18048);

		cfg = new TomlConfigHandler(BetterThanWolves.MOD_ID, TOML);
	}
}
