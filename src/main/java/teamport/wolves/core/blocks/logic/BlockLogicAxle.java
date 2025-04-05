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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.items.WolvesItems;
import teamport.wolves.core.util.BlockPosition;

import java.util.Random;

public class BlockLogicAxle extends BlockLogicAxisAligned {
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
		world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
	}

	@Override
	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		Axis axis = side.getAxis();
		setPowerLevel(world, x, y, z, 0);
		world.setBlockMetadataWithNotify(x, y, z, axisToMeta(axis));
		world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		validatePowerLevel(world, x, y, z);
	}

	private void validatePowerLevel(World world, int x, int y, int z) {
		int currPow = getPowerLevel(world, x, y, z);
		Axis axis = getAxisAlignment(world, x, y, z);

		if (currPow != 3) {
			BlockPosition[] potSrcs = new BlockPosition[2];
			potSrcs[0] = new BlockPosition(x, y, z);
			potSrcs[1] = new BlockPosition(x, y, z);

			switch (axis) {
				case Z:
					potSrcs[0].addOffset(0);
					potSrcs[1].addOffset(1);
					break;
				case Y:
					potSrcs[0].addOffset(2);
					potSrcs[1].addOffset(3);
					break;
				default:
					potSrcs[0].addOffset(4);
					potSrcs[1].addOffset(5);
					break;
			}

			int maxNeighborPow = 0;
			int greaterPowerNeighbors = 0;
			for (int tempSrc = 0; tempSrc < 2; tempSrc++) {
				int tempId = world.getBlockId(potSrcs[tempSrc].x, potSrcs[tempSrc].y, potSrcs[tempSrc].z);

				// A check for if the neighbors AREN'T axles.
				if (tempId != WolvesBlocks.AXLE.id()) {
					continue;
				}

				// This checks if the neighboring axles are the same direction as this one.
				Axis tempAxis = getAxisAlignment(world, potSrcs[tempSrc].x, potSrcs[tempSrc].y, potSrcs[tempSrc].z);
				if (tempAxis != axis) {
					continue;
				}

				// Increase neighbor count.
				int tempPower = getPowerLevel(world, potSrcs[tempSrc].x, potSrcs[tempSrc].y, potSrcs[tempSrc].z);
				if (tempPower > maxNeighborPow) {
					maxNeighborPow = tempPower;
				}
				if (tempPower > currPow) {
					greaterPowerNeighbors++;
				}

				// If there's more than 2 neighbors, explode the axle.
				if (greaterPowerNeighbors >= 2) {
					breakAxle(world, x, y, z);
					return;
				}

				int newPow;
				if (maxNeighborPow > currPow) {
					if (maxNeighborPow == 1) {
						breakAxle(world, x, y, z);
						return;
					}

					newPow = maxNeighborPow - 1;
				} else {
					newPow = 0;
				}

				if (newPow != currPow) {
					setPowerLevel(world, x, y, z, newPow);
				}
			}
		}
	}

	private void setPowerLevel(World world, int x, int y, int z, int powLvl) {
		powLvl &= 3;
		int meta = world.getBlockMetadata(x, y, z) & 3;
		meta |= powLvl;
		world.setBlockMetadataWithNotify(x, y, z, meta);
	}

	private void breakAxle(World world, int x, int y, int z) {
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
		return world.getBlockMetadata(x, y, z) & 3;
	}

	private Axis getAxisAlignment(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z) >> 2;
		return metaToAxis(meta);
	}

	@Override
	public void animationTick(World world, int x, int y, int z, Random rand) {
		if (getPowerLevel(world, x, y, z) > 0) {
			emitAxleParticles(world, x, y, z, rand);
		}
	}

	private void emitAxleParticles(World world, int x, int y, int z, Random rand) {
		if (world.isClientSide) {
			for (int i = 0; i < 2; i++) {
				float smokeX = (float) x + rand.nextFloat();
				float smokeY = (float) y + rand.nextFloat() * 0.5F + 0.625F;
				float smokeZ = (float) z + rand.nextFloat();
				world.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D, 0);
			}
		}
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
}
