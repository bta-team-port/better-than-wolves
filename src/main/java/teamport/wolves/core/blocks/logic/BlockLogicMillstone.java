package teamport.wolves.core.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import teamport.wolves.BetterThanWolves;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.blocks.entity.TileEntityMillstone;
import teamport.wolves.core.util.BlockPosition;
import teamport.wolves.core.util.IMechanicalDevice;
import teamport.wolves.core.util.InventoryHandler;

import java.util.Random;

public class BlockLogicMillstone extends BlockLogic implements IMechanicalDevice {
	public BlockLogicMillstone(Block<?> block) {
		super(block, Material.stone);
		block.withEntity(TileEntityMillstone::new);
	}

	@Override
	public int tickDelay() {
		return 10;
	}

	@Override
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		super.onBlockPlacedByMob(world, x, y, z, side, mob, xPlaced, yPlaced);
		world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
		TileEntityMillstone tileEntity = (TileEntityMillstone) world.getTileEntity(x, y, z);

		if (tileEntity == null || tileEntity.isInvalid()) {
			world.setBlockWithNotify(x, y, z, 0);
			BetterThanWolves.LOGGER.error("Tile entity at {}, {}, {} was null or invalid and has been removed!", x, y, z);
			return false;
		}

		Catalyst.displayGui(player, tileEntity, "wolves:gui/millstone");

		return true;
	}

	public boolean isBlockOn(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) > 0;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		boolean isInserting = isInputtingMechanicalPower(world, x, y, z);

		if (isBlockOn(world, x, y, z) != isInserting) {
			world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		boolean isReceiving = isInputtingMechanicalPower(world, x, y, z);
		boolean blockOn = isBlockOn(world, x, y, z);

		if (blockOn != isReceiving) {
			if (blockOn) {
				setBlockOn(world, x, y, z, false);
			} else {
				TileEntityMillstone tileEntity = (TileEntityMillstone) world.getTileEntity(x, y, z);
				if (tileEntity.IsWholeCompanionCubeNextToBeProcessed()) {
					world.playSoundEffect(null,
						SoundCategory.ENTITY_SOUNDS,
						(float) x + 0.5F,
						(float) y + 0.5F,
						(float) z + 0.5F,
						"mob.wolf.hurt",
						5.0F,
						(world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);
				}

				world.playSoundEffect(null,
					SoundCategory.ENTITY_SOUNDS,
					(float) x + 0.5F,
					(float) y + 0.5F,
					(float) z + 0.5F,
					"random.explode",
					0.2F,
					1.25F);

				emitMillingParticles(world, x, y, z, rand);
				setBlockOn(world, x, y, z, true);
			}
		}
	}

	private void emitMillingParticles(World world, int x, int y, int z, Random rand) {
		for (int i = 0; i < 5; i++) {
			float smokeX = (float)x + rand.nextFloat();
			float smokeY = (float)y + rand.nextFloat() * 0.5F + 1.0F;
			float smokeZ = (float)z + rand.nextFloat();
			world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0, 0);
		}
	}

	private void setBlockOn(World world, int x, int y, int z, boolean active) {
		world.setBlockMetadataWithNotify(x, y, z, active ? 1 : 0);
	}

	@Override
	public boolean canOutputMechanicalPower() {
		return false;
	}

	@Override
	public boolean canInputMechanicalPower() {
		return true;
	}

	@Override
	public boolean isOutputtingMechanicalPower(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public boolean isInputtingMechanicalPower(World world, int x, int y, int z) {
		for (int iA = 0; iA < 2; iA++) {
			BlockPosition pos = new BlockPosition(x, y, z);
			pos.addOffset(iA);

			int target = world.getBlockId(pos.x, pos.y, pos.z);
			if (target != WolvesBlocks.AXLE.id()) {
				continue;
			}


			BlockLogicAxle axle = world.getBlockLogic(pos.x, pos.y, pos.z, BlockLogicAxle.class);
			if (axle == null) {
				return false;
			}

			if (axle.isAxleOriented(world, x, y, z, Axis.Y) && axle.getPowerLevel(world, pos.x, pos.y, pos.z) > 0) {
				return true;
			}
		}

		for (int iA = 2; iA < 6; iA++) {
			BlockPosition pos = new BlockPosition(x, y, z);
			pos.addOffset(iA);

			int target = world.getBlockId(pos.x, pos.y, pos.z);
			if (target != WolvesBlocks.HAND_CRANK.id()) {
				continue;
			}

			Block<?> targetBlock = Blocks.blocksList[target];
			if (targetBlock == null) {
				return false;
			}

			if (((IMechanicalDevice) targetBlock.getLogic()).isOutputtingMechanicalPower(world, pos.x, pos.y, pos.z)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void animationTick(World world, int x, int y, int z, Random rand) {
		if (!isBlockOn(world, x, y, z)) {
			return;
		}

		emitMillingParticles(world, x, y, z, rand);
		if (rand.nextInt(5) == 0) {
			world.playSoundEffect(null,
				SoundCategory.ENTITY_SOUNDS,
				(float) x + 0.5F,
				(float) y + 0.5F,
				(float) z + 0.5F,
				"random.explode",
				0.2F,
				1.25F);
		}
	}

	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		super.onBlockRemoved(world, x, y, z, data);
		TileEntityMillstone tileEntity = (TileEntityMillstone) world.getTileEntity(x, y, z);
		InventoryHandler.dropTileContents(world, x, y, z, tileEntity);
	}
}
