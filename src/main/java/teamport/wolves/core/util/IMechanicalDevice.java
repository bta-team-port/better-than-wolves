package teamport.wolves.core.util;

import net.minecraft.core.world.World;

public interface IMechanicalDevice {
	boolean canOutputMechanicalPower();

	boolean canInputMechanicalPower();

	boolean isOutputtingMechanicalPower(World world, int x, int y, int z);

	boolean isInputtingMechanicalPower(World world, int x, int y, int z);
}
