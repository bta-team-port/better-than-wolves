package teamport.wolves.core.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSlab;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import teamport.wolves.core.WolvesConfig;
import teamport.wolves.core.blocks.logic.*;
import turniplabs.halplibe.helper.BlockBuilder;

import static teamport.wolves.BetterThanWolves.MOD_ID;

public class WolvesBlocks {
	private static int blockID = WolvesConfig.cfg.getInt("IDs.blockIDs");

	private static int nextID() {
		return blockID++;
	}

	public static final Block<BlockLogic> SUN_LAMP_IDLE;
	public static final Block<BlockLogic> SUN_LAMP_ACTIVE;
	public static final Block<BlockLogic> COMPANION_CUBE;
	public static final Block<BlockLogic> COMPANION_MEAT;
	public static final Block<BlockLogicSlab> COMPANION_SLAB;
	public static final Block<BlockLogic> HAND_CRANK;
	public static final Block<BlockLogicAxle> AXLE;
	public static final Block<BlockLogicMillStone> MILL_STONE;

	static {
		SUN_LAMP_IDLE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.GLASS)
			.setHardness(0.3F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("sunlamp.idle", nextID(), b -> new BlockLogicSunLamp(b, false));

		SUN_LAMP_ACTIVE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.GLASS)
			.setHardness(0.3F)
			.setLuminance(1)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("sunlamp.active", nextID(), b -> new BlockLogicSunLamp(b, false))
			.withLightEmission(1.0F);

		COMPANION_CUBE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.CLOTH)
			.setHardness(1.0F)
			.setTags(BlockTags.MINEABLE_BY_SWORD)
			.build("companioncube", nextID(), BlockLogicCompanionCube::new);

		COMPANION_MEAT = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.CLOTH)
			.setHardness(1.0F)
			.setTags(BlockTags.MINEABLE_BY_SWORD, BlockTags.NOT_IN_CREATIVE_MENU)
			.build("companionmeat", nextID(), BlockLogicCompanionCube::new);

		COMPANION_SLAB = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.CLOTH)
			.setHardness(1.0f)
			.setTags(BlockTags.MINEABLE_BY_SWORD)
			.build("companionslab", nextID(), b -> new BlockLogicSlab(b, COMPANION_MEAT));

		HAND_CRANK = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(3.5F)
			.setTicking(true)
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.MINEABLE_BY_PICKAXE)
			.build("handcrank", nextID(), BlockLogicHandCrank::new);

		AXLE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.WOOD)
			.setHardness(1.0F)
			.setTicking(true)
			.setTags(BlockTags.MINEABLE_BY_AXE)
			.build("axle", nextID(), BlockLogicAxle::new);

		MILL_STONE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(3.5F)
			.setTicking(true)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("millstone", nextID(), BlockLogicMillStone::new);
	}
}
