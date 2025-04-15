package teamport.wolves.core.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicAxisAligned;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.PlacementMode;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.items.WolvesItems;

import java.util.Random;

public class BlockLogicAxle extends BlockLogicAxisAligned {
	private static final int MASK_POWER = 0b1111_1100;
	private static final int MASK_AXIS = 0b0000_0011;
	private static boolean shouldSignal;

	public BlockLogicAxle(Block<?> block) {
		super(block, Material.wood);
	}

	@Override
	public int tickDelay() {
		return 1;
	}

	@Override
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		Axis axis = mob.getPlacementDirection(side, PlacementMode.SIDE).getAxis();

		setPowerLevel(world, x, y, z, 0);
		world.setBlockMetadataWithNotify(x, y, z, axisToMeta(axis));
		world.scheduleBlockUpdate(x, y, z, this.block.id(), tickDelay());
	}

	@Override
	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		Axis axis = side.getAxis();

		setPowerLevel(world, x, y, z, 0);
		world.setBlockMetadataWithNotify(x, y, z, axisToMeta(axis));
		world.scheduleBlockUpdate(x, y, z, this.block.id(), tickDelay());
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
			validatePowerLevel(world, x, y, z);
	}

	private void validatePowerLevel(World world, int x, int y, int z) {
		int currPower = getPowerLevel(world, x, y, z);
		int meta = world.getBlockMetadata(x, y, z) & MASK_AXIS;
		Axis axis = metaToAxis(meta);

		if (currPower > 0) {
			emitParticles(world, x, y, z, world.rand);
		}

		if (currPower != 3) {
			Side[] sides = new Side[2];
			switch (axis) {
				case Y:
					sides[0] = Side.TOP;
					sides[1] = Side.BOTTOM;
					break;
				case Z:
					sides[0] = Side.NORTH;
					sides[1] = Side.SOUTH;
					break;
				default:
					sides[0] = Side.EAST;
					sides[1] = Side.WEST;
					break;
			}

			int srcCount = 0;
			int srcPower = 0;
			for (Side side : sides) {
				if (world.getBlockId(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ()) != WolvesBlocks.AXLE.id()) {
					continue;
				}

				int otherMeta = world.getBlockMetadata(x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ()) & MASK_AXIS;
				if (metaToAxis(otherMeta) != axis) {
					continue;
				}

				int tempPower = WolvesBlocks.AXLE.getLogic().getPowerLevel(world, x + side.getOffsetX(), y + side.getOffsetY(), z + side.getOffsetZ());
				if (tempPower > srcPower) {
					srcPower = tempPower;
				}

				if (tempPower > currPower) {
					srcCount++;
				}
			}

			if (srcCount >= 2) {
				breakAxle(world, x, y, z);
				return;
			}

			int newPower;
			if (srcPower > currPower) {
				if (srcPower == 1) {
					breakAxle(world, x, y, z);
					return;
				}

				newPower = srcPower - 1;
			} else {
				newPower = 0;
			}

			if (newPower != currPower) {
				setPowerLevel(world, x, y, z, newPower);
			}
		}
	}

	public void setPowerLevel(World world, int x, int y, int z, int power) {
		int meta = world.getBlockMetadata(x, y, z);
		world.setBlockMetadataWithNotify(x, y, z, (meta & ~MASK_POWER) | ((power << 2) & MASK_POWER));
	}

	public void breakAxle(World world, int x, int y, int z) {
		dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, 0, null, null);
		world.playSoundEffect(null,
			SoundCategory.WORLD_SOUNDS,
			(double) x + 0.5D,
			(double) y + 0.5D,
			(double) z + 0.5D,
			"random.explode",
			0.2F,
			1.25F);

		world.setBlockWithNotify(x, y, z, 0);
	}

	public int getPowerLevel(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		return (meta & MASK_POWER) >> 2;
	}

	private Axis getAxisAlignment(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z) & MASK_AXIS;
		return metaToAxis(meta);
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
		Random rand = world.rand;

		switch (dropCause) {
			case WORLD:
			case EXPLOSION:
				return new ItemStack[]{new ItemStack(Items.STICK, rand.nextInt(2)), new ItemStack(WolvesItems.HEMP_FIBER, rand.nextInt(5))};
		}

		return super.getBreakResult(world, dropCause, x, y, z, meta, tileEntity);
	}

	public boolean isAxleOriented(World world, int x, int y, int z, Axis axis) {
		Axis axisAlignment = getAxisAlignment(world, x, y, z);
		return axis == axisAlignment;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		world.scheduleBlockUpdate(x, y, z, this.block.id(), tickDelay());
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

	public boolean isAxleOriented(WorldSource blockAccess, int x, int y, int z, Axis axis) {
		int meta = blockAccess.getBlockMetadata(x, y, z) & MASK_AXIS;
		return metaToAxis(meta) == axis;
	}
}
