package teamport.wolves.core.blocks.logic;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import teamport.wolves.BetterThanWolves;
import teamport.wolves.core.blocks.entity.TileEntityCauldron;
import teamport.wolves.core.util.InventoryHandler;

import java.util.Random;

public class BlockLogicCauldron extends BlockLogic {
	public BlockLogicCauldron(Block<?> block) {
		super(block, Material.metal);
		block.withEntity(TileEntityCauldron::new);
	}

	@Override
	public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
		super.onBlockPlacedByMob(world, x, y, z, side, mob, xPlaced, yPlaced);
		world.scheduleBlockUpdate(x, y, z, this.id(), tickDelay());
	}

	@Override
	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		TileEntityCauldron tileEntity = (TileEntityCauldron) world.getTileEntity(x, y, z);
		InventoryHandler.dropTileContents(world, x, y, z, tileEntity);
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
		TileEntityCauldron tileEntity = (TileEntityCauldron) world.getTileEntity(x, y, z);

		if (tileEntity == null || tileEntity.isInvalid()) {
			world.setBlockWithNotify(x, y, z, 0);
			BetterThanWolves.LOGGER.error("Tile entity at {}, {}, {} was null or invalid and has been removed!", x, y, z);
			return false;
		}

		Catalyst.displayGui(player, tileEntity, "wolves:gui/cauldron");

		return true;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		if (entity instanceof EntityItem) {
			TileEntityCauldron tileEntity = (TileEntityCauldron) world.getTileEntity(x, y, z);

			Item item = ((EntityItem) entity).item.getItem();
			int itemStack = ((EntityItem) entity).item.stackSize;
			int itemMeta = ((EntityItem) entity).item.getMetadata();
			CompoundTag itemTag = ((EntityItem) entity).item.getData();

			if (InventoryHandler.addItemStackToInventory(tileEntity, new ItemStack(item, itemStack, itemMeta, itemTag))) {
				entity.remove();
				world.playSoundEffect(null,
					SoundCategory.WORLD_SOUNDS,
					x + 0.5,
					y + 0.5,
					z + 0.5,
					"random.pop",
					0.25F,
					((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			}
		}
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		validateFire(world, x, y, z);
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		validateFire(world, x, y, z);
	}

	private void validateFire(World world, int x, int y, int z) {
		int oldState = getFireUnderState(world, x, y, z);
		int newState = 0;

		// TODO - Add stoked fire with newState 2!
		if (world.getBlockId(x, y - 1, z) == Blocks.FIRE.id()) {
			newState = 1;
		}

		if (newState != oldState) {
			setFireUnderState(world, x, y, z, newState);
			TileEntityCauldron tileEntity = (TileEntityCauldron) world.getTileEntity(x, y, z);
			tileEntity.notifyOfFireChange(newState);
		}
	}

	public int getFireUnderState(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) & 3;
	}

	private void setFireUnderState(World world, int x, int y, int z, int state) {
		int meta = world.getBlockMetadata(x, y, z) & -4;
		meta |= state &3;
		world.setBlockMetadataWithNotify(x, y, z, meta);
	}
}
