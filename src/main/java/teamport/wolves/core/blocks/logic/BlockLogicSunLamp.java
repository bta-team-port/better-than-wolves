package teamport.wolves.core.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.blocks.WolvesBlocks;

public class BlockLogicSunLamp extends BlockLogic {
	public boolean isActive;

	public BlockLogicSunLamp(Block<?> block, boolean isActive) {
		super(block, Material.glass);
		this.isActive = isActive;
	}

	@Override
	public void onBlockPlacedByWorld(World world, int x, int y, int z) {
		if (!world.isClientSide) {
			boolean hasSignal = world.hasDirectSignal(x, y, z) || world.hasNeighborSignal(x, y, z);

			if (hasSignal && !isActive) {
				world.setBlockWithNotify(x, y, z, WolvesBlocks.SUN_LAMP_ACTIVE.id());
			} else if (hasSignal) {
				world.setBlockWithNotify(x, y, z, WolvesBlocks.SUN_LAMP_IDLE.id());
			}
		}
	}

	@Override
	public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
		if (!world.isClientSide) {
			boolean hasSignal = world.hasDirectSignal(x, y, z) || world.hasNeighborSignal(x, y, z);

			if (hasSignal) {
				world.setBlockWithNotify(x, y, z, WolvesBlocks.SUN_LAMP_ACTIVE.id());
			}
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (!world.isClientSide) {
			boolean hasSignal = world.hasDirectSignal(x, y, z) || world.hasNeighborSignal(x, y, z);

			if (hasSignal) {
				if (!isActive) {
					world.setBlockWithNotify(x, y, z, WolvesBlocks.SUN_LAMP_ACTIVE.id());
				}
			} else {
				world.setBlockWithNotify(x, y, z, WolvesBlocks.SUN_LAMP_IDLE.id());
			}
		}
	}

	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
		return new ItemStack[]{WolvesBlocks.SUN_LAMP_IDLE.getDefaultStack()};
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}
}
