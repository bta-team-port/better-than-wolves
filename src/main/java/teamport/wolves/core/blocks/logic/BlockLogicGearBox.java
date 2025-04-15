package teamport.wolves.core.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.util.IMechanicalDevice;

import java.util.Random;

public class BlockLogicGearBox extends BlockLogic implements IMechanicalDevice {
	private static final int MASK_AXIS = 0b0000_0111;
	private static final int MASK_POWERED = 0b0000_1000;

	public BlockLogicGearBox(Block<?> block) {
		super(block, Material.wood);
	}

	@Override
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		Direction placementDir = mob.getPlacementDirection(side).getOpposite();
		world.setBlockMetadataWithNotify(x, y, z, (placementDir.getId() & MASK_AXIS));
		world.scheduleBlockUpdate(x, y, z, this.block.id(), tickDelay());
	}

	@Override
	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		Direction placementDir = side.getDirection();
		world.setBlockMetadataWithNotify(x, y, z, placementDir.getId());
		world.scheduleBlockUpdate(x, y, z, this.block.id(), tickDelay());
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		boolean isInputting = isInputtingMechanicalPower(world, x, y, z);
		boolean isRedstonePowered = world.hasDirectSignal(x, y, z) || world.hasNeighborSignal(x, y, z);
		boolean isActive = getIsPowered(world, x, y, z);

		if (isInputting) {
			validateAxles(world, x, y, z, false);
			setPowerState(world, x, y, z, !isRedstonePowered);
			world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, x, y, z, "random.explode", 0.05F, 1.0F);
		} else {
			validateAxles(world, x, y, z, true);
		}
	}

	private void validateAxles(World world, int x, int y, int z, boolean destroyAxleIfPowered) {
		Direction facing = getDirection(world.getBlockMetadata(x, y, z));

		for (Direction direction : Direction.directions) {
			if (direction == facing) {
				continue;
			}

			if (world.getBlockId(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ()) != WolvesBlocks.AXLE.id()) {
				continue;
			}

			BlockLogicAxle axle = WolvesBlocks.AXLE.getLogic();
			if (!axle.isAxleOriented(world, x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ(), direction.getAxis())) {
				continue;
			}

			int axlePower = axle.getPowerLevel(world, x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ());
			if (axlePower > 0 && destroyAxleIfPowered) {
				WolvesBlocks.AXLE.getLogic().breakAxle(world, x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ());
			}

			if (getIsPowered(world, x, y, z)) {
				if (axlePower != 3) {
					axle.setPowerLevel(world, x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ(), 3);
				}
			} else {
				if (axlePower != 0) {
					axle.setPowerLevel(world, x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ(), 0);
				}
			}
		}
	}

	@Override
	public boolean canOutputMechanicalPower() {
		return true;
	}

	@Override
	public boolean canInputMechanicalPower() {
		return true;
	}

	@Override
	public boolean isOutputtingMechanicalPower(World world, int x, int y, int z) {
		return getIsPowered(world, x, y, z);
	}

	@Override
	public boolean isInputtingMechanicalPower(World world, int x, int y, int z) {
		Direction facing = getDirection(world.getBlockMetadata(x, y, z));

			BlockLogicAxle axle = WolvesBlocks.AXLE.getLogic();
			if (world.getBlockId(x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ()) == WolvesBlocks.AXLE.id()) {
				if (axle.isAxleOriented(world, x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ(), facing.getAxis())) {
					return axle.getPowerLevel(world, x + facing.getOffsetX(), y + facing.getOffsetY(), z + facing.getOffsetZ()) > 0;
				}
			}
		return false;
	}

	private boolean getIsPowered(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return (meta & MASK_POWERED) != 0;
	}

	private void setPowerState(World world, int x, int y, int z, boolean flag) {
		int meta = world.getBlockMetadata(x, y, z);

		if (flag) {
			world.setBlockMetadataWithNotify(x, y, z, (meta | MASK_POWERED));
		} else {
			world.setBlockMetadataWithNotify(x, y, z, (meta & ~MASK_POWERED));
		}
	}

	private void emitParticles(World world, int i, int j, int k, Random random) {
		for(int counter = 0; counter < 2; counter++)
		{
			float smokeX = (float)i + random.nextFloat();
			float smokeY = (float)j + random.nextFloat() * 0.5F + 0.625F;
			float smokeZ = (float)k + random.nextFloat();
			world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D, 0);
		}
	}

	public Direction getDirection(int meta) {
		return Direction.getDirectionById(meta & MASK_AXIS);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		world.scheduleBlockUpdate(x, y, z, this.block.id(), tickDelay());
	}
}
