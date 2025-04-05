package teamport.wolves.core.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.world.World;

import java.util.Random;

public class BlockLogicCompanionCube extends BlockLogicRotatable {
	public BlockLogicCompanionCube(Block<?> block) {
		super(block, Material.cloth);
	}

	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		super.onBlockRemoved(world, x, y, z, data);
		Random random = new Random();
		world.playSoundEffect(null,
			SoundCategory.ENTITY_SOUNDS,
			x,
			y,
			z,
			"mob.wolf.whine",
			0.3F,
			(random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
	}
}
