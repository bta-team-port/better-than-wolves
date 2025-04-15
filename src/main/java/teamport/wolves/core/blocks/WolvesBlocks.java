package teamport.wolves.core.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicCake;
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
	public static final Block<BlockLogicMillstone> MILLSTONE;
	public static final Block<BlockLogic> CAULDRON;
	public static final Block<BlockLogicAxle> AXLE;
	public static final Block<BlockLogicGearBox> GEAR_BOX;
//	public static final Block<BlockLogic> FIRE_STOKED;
	public static final Block<BlockLogicCake> CAKE_PLAIN;
	public static final Block<BlockLogicCake> CAKE_CHOCOLATE;
	public static final Block<BlockLogicCake> CAKE_SPRINKLES;

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
			.setTags(BlockTags.MINEABLE_BY_PICKAXE, BlockTags.NOT_IN_CREATIVE_MENU)
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
			.setTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.MINEABLE_BY_PICKAXE)
			.build("handcrank", nextID(), BlockLogicHandCrank::new);

		MILLSTONE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.STONE)
			.setHardness(3.5F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("millstone", nextID(), BlockLogicMillstone::new);

		CAULDRON = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.METAL)
			.setHardness(10.0F)
			.setTags(BlockTags.MINEABLE_BY_PICKAXE)
			.build("cauldron", nextID(), BlockLogicCauldron::new);

		AXLE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.WOOD)
			.setHardness(1.0F)
			.setTags(BlockTags.MINEABLE_BY_AXE)
			.build("axle", nextID(), BlockLogicAxle::new)
			.withDisabledNeighborNotifyOnMetadataChange();

		GEAR_BOX = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.WOOD)
			.setHardness(1.0F)
			.setTags(BlockTags.MINEABLE_BY_AXE)
			.build("gearbox", nextID(), BlockLogicGearBox::new)
			.withDisabledNeighborNotifyOnMetadataChange();

		CAKE_PLAIN = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.CLOTH)
			.setHardness(0.5F)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.BROKEN_BY_FLUIDS)
			.build("cake.plain", nextID(), BlockLogicCake::new);

		CAKE_CHOCOLATE = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.CLOTH)
			.setHardness(0.5F)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.BROKEN_BY_FLUIDS)
			.build("cake.chocolate", nextID(), BlockLogicCake::new);

		CAKE_SPRINKLES = new BlockBuilder(MOD_ID)
			.setBlockSound(BlockSounds.CLOTH)
			.setHardness(0.5F)
			.setTags(BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.BROKEN_BY_FLUIDS)
			.build("cake.sprinkles", nextID(), BlockLogicCake::new);
	}
}
