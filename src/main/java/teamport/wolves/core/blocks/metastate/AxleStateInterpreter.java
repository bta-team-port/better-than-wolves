package teamport.wolves.core.blocks.metastate;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;

import java.util.HashMap;

public class AxleStateInterpreter extends MetaStateInterpreter {
	@Override
	public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block<?> block, int meta) {
		HashMap<String, String> result = new HashMap<>();

		int axis = (meta & 0b0000_0011);
		result.put("axis", String.valueOf(axis));

		return result;
	}
}
