package teamport.wolves.core.util;

import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.World;

import java.util.Objects;
import java.util.Random;

public class InventoryHandler {

	public static void dropTileContents(World world, int x, int y, int z, Container tile) {
		Random rand = new Random();
		for (int invSize = 0; invSize < tile.getContainerSize(); ++invSize) {
			ItemStack itemstack = tile.getItem(invSize);
			if (itemstack != null) {
				float xRand = rand.nextFloat() * 0.8F + 0.1F;
				float yRand = rand.nextFloat() * 0.8F + 0.1F;
				float zRand = rand.nextFloat() * 0.8F + 0.1F;

				while (itemstack.stackSize > 0) {
					int i1 = rand.nextInt(21) + 10;
					if (i1 > itemstack.stackSize) {
						i1 = itemstack.stackSize;
					}

					itemstack.stackSize -= i1;
					EntityItem entityItem = new EntityItem(world,
						(float) x + xRand,
						(float) y + yRand,
						(float) z + zRand,
						new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));

					float mult = 0.05F;
					entityItem.xd = (float) rand.nextGaussian() * mult;
					entityItem.yd = (float) rand.nextGaussian() * mult + 0.2F;
					entityItem.zd = (float) rand.nextGaussian() * mult;
					world.entityJoinedWorld(entityItem);
				}
			}
		}
	}

	public static boolean addItemStackToInventory(Container tile, ItemStack stack) {
		if (stack.getMetadata() <= 0) {
			stack.stackSize = storePartialItemInstance(tile, stack);
			if (stack.stackSize <= 0) {
				return true;
			}
		}

		int slot = getFirstEmptyStack(tile);
		if (slot >= 0) {
			tile.setItem(slot, stack);
			return true;
		} else {
			return false;
		}
	}

	private static int getFirstEmptyStack(Container tile)
	{
		for(int i = 0; i < tile.getContainerSize(); i++) {
			if(tile.getItem(i) == null) {
				return i;
			}
		}

		return -1;
	}

	private static int storePartialItemInstance(Container tile, ItemStack stack) {
		int stackId = stack.itemID;
		int stackSize = stack.stackSize;
		int slot = findValidSlotForItem(tile, stack);
		if(slot < 0) {
			slot = getFirstEmptyStack(tile);
		}

		if(slot < 0) {
			return stackSize;
		}

		if(tile.getItem(slot) == null) {
			tile.setItem(slot, new ItemStack(stackId, 0, stack.getMetadata()));
		}

		int insertedItems = stackSize;
		ItemStack tempStack = tile.getItem(slot);

		if (tempStack == null) {
			return stackSize;
		}

		if (insertedItems > tempStack.getMaxStackSize() - tempStack.stackSize) {
			insertedItems = tempStack.getMaxStackSize() - tempStack.stackSize;
		}
		if (insertedItems > tile.getMaxStackSize() - tempStack.stackSize) {
			insertedItems = tile.getMaxStackSize() - tempStack.stackSize;
		}

		if (insertedItems != 0) {
			stackSize -= insertedItems;
			tempStack.stackSize += insertedItems;
			tile.setItem(slot, tempStack);
		}
		return stackSize;
	}

	private static int findValidSlotForItem(Container tile, ItemStack stack)
	{
		for(int slot = 0; slot < tile.getContainerSize(); slot++)
		{
			ItemStack tempStack = tile.getItem(slot);
			if(tempStack != null && tempStack.itemID == stack.itemID && tempStack.isStackable() && tempStack.stackSize < tempStack.getMaxStackSize() && tempStack.stackSize < tile.getMaxStackSize() && (tempStack.getMetadata() == stack.getMetadata())) {
				return slot;
			}
		}

		return -1;
	}

	public static int getFirstOccupiedSlot(Container tile, int itemID) {
		for (int i = 0; i < tile.getContainerSize(); i++) {
			if (tile.getItem(i) != null && Objects.requireNonNull(tile.getItem(i)).itemID == itemID) {
				return i;
			}
		}

		return -1;
	}

	public static void dropWithRandomOffset(World world, int x, int y, int z, ItemStack stack) {
		float xOffset = world.rand.nextFloat() * 0.7F + 0.15F;
		float yOffset = world.rand.nextFloat() * 0.2F + 0.1F;
		float zOffset = world.rand.nextFloat() * 0.7F + 0.15F;
		EntityItem item = new EntityItem(world, x + xOffset, y + yOffset, z + zOffset, stack);
		float velocity = 0.05F;
		item.xo = (float)world.rand.nextGaussian() * velocity;
		item.yo = (float)world.rand.nextGaussian() * velocity + 0.2F;
		item.zo = (float)world.rand.nextGaussian() * velocity;

		item.pickupDelay = 10;
		world.entityJoinedWorld(item);
	}

	public static int itemCountInInventory(Container inventory, int itemID, int meta) {
		int itemCount = 0;
		for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
			ItemStack tempStack = inventory.getItem(slot);

			if (tempStack != null && tempStack.itemID == itemID && (meta == -1 || tempStack.getMetadata() == meta) ) {
				itemCount += inventory.getItem(slot).stackSize;
			}
		}

		return itemCount;
	}
}
