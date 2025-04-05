package teamport.wolves.core.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.util.IMechanicalDevice;
import teamport.wolves.core.items.WolvesItems;

import java.util.Random;

public class BlockLogicHandCrank extends BlockLogic implements IMechanicalDevice {
	public BlockLogicHandCrank(Block<?> block) {
		super(block, Material.piston);
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
		if (world == null) {
			return true;
		}

		int meta = world.getBlockMetadata(x, y, z);
		if (meta == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
			world.playSoundEffect(null,
				SoundCategory.WORLD_SOUNDS,
				(double)x + 0.5D,
				(double)y + 0.5D,
				(double)z + 0.5D,
				"random.click",
				1.0F,
				2.0F);
			world.notifyBlocksOfNeighborChange(x, y, z, this.id());
			world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
		}

		return true;
	}

	@Override
	public int tickDelay() {
		return 3;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta > 0 && meta < 7) {
			world.playSoundEffect(null,
				SoundCategory.WORLD_SOUNDS,
				(double) x + 0.5D,
				(double) y + 0.5D,
				(double) z + 0.5D,
				"random.click",
				1.0F,
				2.0F);

			world.scheduleBlockUpdate(x, y, z, this.id(), meta <= 5 ? tickDelay() + meta : 15);

			world.setBlockMetadataWithNotify(x, y, z, meta + 1);
		} else if (meta >= 7) {
			world.setBlockMetadataWithNotify(x, y, z, 0);
			world.playSoundEffect(null,
				SoundCategory.WORLD_SOUNDS,
				(double) x + 0.5D,
				(double) y + 0.5D,
				(double) z + 0.5D,
				"random.click",
				0.3F,
				0.7F);
			world.notifyBlocksOfNeighborChange(x, y, z, this.id());
		}
	}

	@Override
	public ItemStack @Nullable [] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {

		switch (dropCause) {
			case WORLD:
			case EXPLOSION:
			case IMPROPER_TOOL:
				return new ItemStack[]{Items.STICK.getDefaultStack(), new ItemStack(Blocks.COBBLE_STONE, 2), WolvesItems.GEAR.getDefaultStack()};
		}

		return super.getBreakResult(world, dropCause, x, y, z, meta, tileEntity);
	}

	@Override
	public AABB getCollisionBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
		return AABB.getPermanentBB(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F).move(x, y, z);
	}

	@Override
	public AABB getSelectedBoundingBoxFromPool(WorldSource world, int x, int y, int z) {
		return AABB.getPermanentBB(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F).move(x, y, z);
	}

	@Override
	public boolean canOutputMechanicalPower() {
		return true;
	}

	@Override
	public boolean canInputMechanicalPower() {
		return false;
	}

	@Override
	public boolean isOutputtingMechanicalPower(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) > 0;
	}

	@Override
	public boolean isInputtingMechanicalPower(World world, int x, int y, int z) {
		return false;
	}

	@Override
	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 1);
			world.playSoundEffect(null,
				SoundCategory.WORLD_SOUNDS,
				(double)x + 0.5D,
				(double)y + 0.5D,
				(double)z + 0.5D,
				"random.click",
				1.0F,
				2.0F);
			world.notifyBlocksOfNeighborChange(x, y, z, this.id());
			world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
		}
	}
}
