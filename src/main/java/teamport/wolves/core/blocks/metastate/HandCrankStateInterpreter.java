package teamport.wolves.core.blocks.metastate;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;

import java.util.HashMap;

public class HandCrankStateInterpreter extends MetaStateInterpreter {
	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block<?> block, int meta) {
		HashMap<String, String> result = new HashMap<>();

		result.put("crank", meta > 0 ? "true" : "false");

		return result;
	}
}
