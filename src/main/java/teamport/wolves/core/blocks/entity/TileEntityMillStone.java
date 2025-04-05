package teamport.wolves.core.blocks.entity;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.sound.SoundCategory;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.blocks.logic.BlockLogicMillStone;
import teamport.wolves.core.items.WolvesItems;
import teamport.wolves.core.recipes.MillStoneRecipeRegistry;
import teamport.wolves.core.util.BlockPosition;

public class TileEntityMillStone extends TileEntity implements Container {
	ItemStack[] inv = new ItemStack[3];
	public int grindProgress = 0;

	@Override
	public int getContainerSize() {
		return inv.length;
	}

	@Override
	public @Nullable ItemStack getItem(int i) {
		return inv[i];
	}

	public @Nullable ItemStack removeItem(int index, int takeAmount) {
		if (this.inv[index] != null) {
			if (this.inv[index].stackSize <= takeAmount) {
				ItemStack stack = this.inv[index];
				this.inv[index] = null;
				this.setChanged();
				return stack;
			} else {
				ItemStack splitStack = this.inv[index].splitStack(takeAmount);
				if (this.inv[index].stackSize <= 0) {
					this.inv[index] = null;
				}

				this.setChanged();
				return splitStack;
			}
		} else {
			return null;
		}
	}

	public void setItem(int index, @Nullable ItemStack itemstack) {
		this.inv[index] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getMaxStackSize()) {
			itemstack.stackSize = this.getMaxStackSize();
		}

		this.setChanged();
	}

	@Override
	public String getNameTranslationKey() {
		return "wolves.container.millstone.name";
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public boolean stillValid(Player player) {
		if (this.worldObj != null && this.worldObj.getTileEntity(this.x, this.y, this.z) == this) {
			return player.distanceToSqr((double)this.x + (double)0.5F,
				(double)this.y + (double)0.5F,
				(double)this.z + (double)0.5F) <= (double)64.0F;
		} else {
			return false;
		}
	}

	@Override
	public void sortContainer() {
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		super.writeToNBT(tag);
		ListTag items = new ListTag();

		for(int i = 0; i < this.inv.length; ++i) {
			if (this.inv[i] != null) {
				CompoundTag invTag = new CompoundTag();
				invTag.putByte("Slot", (byte)i);
				this.inv[i].writeToNBT(invTag);
				items.addTag(invTag);
			}
		}

		tag.put("Items", items);
		tag.putInt("Grind", grindProgress);
	}

	@Override
	public void readFromNBT(CompoundTag tag) {
		super.readFromNBT(tag);
		ListTag items = tag.getList("Items");
		this.inv = new ItemStack[this.getContainerSize()];
		this.grindProgress = tag.getInteger("Grind");

		for(int i = 0; i < items.tagCount(); ++i) {
			CompoundTag invTag = (CompoundTag)items.tagAt(i);
			int j = invTag.getByte("Slot") & 255;
			if (j < this.inv.length) {
				this.inv[j] = ItemStack.readItemStackFromNbt(invTag);
			}
		}
	}

	public int getGrindProgressScaled(int scale) {
		return (grindProgress * scale) / 200;
	}

	public boolean isGrinding() {
		return grindProgress > 0;
	}

	@Override
	public void tick() {
		if (worldObj == null || worldObj.isClientSide) {
			return;
		}

		int unmilledIndex = getUnmilledInventoryIndex();
		if (unmilledIndex < 0) {
			grindProgress = 0;
			return;
		}

		BlockLogicMillStone block = worldObj.getBlockLogic(x, y, z, BlockLogicMillStone.class);
		if (block == null) {
			return;
		}

		if(!block.isBlockOn(worldObj, x, y, z)) {
			return;
		}

		int storedId = inv[unmilledIndex].itemID;


		grindProgress++;
		if (storedId == WolvesBlocks.COMPANION_CUBE.id()) {
			if(inv[unmilledIndex].getMetadata() == 0 && worldObj.rand.nextInt(10) == 0) {
				worldObj.playSoundEffect(null,
					SoundCategory.ENTITY_SOUNDS,
					(float) x + 0.5F,
					(float) y + 0.5F,
					(float) z + 0.5F,
					"mob.wolf.hurt",
					2.0F,
					(worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
			}

			if (worldObj.rand.nextInt(20) == 0) {
				ItemStack stack = Items.STRING.getDefaultStack();
				ejectStack(stack);
			}

			if (worldObj.rand.nextInt(60) == 0) {
				ItemStack stack = new ItemStack(Blocks.WOOL, 1, 14);
				ejectStack(stack);
			}
		} else if (storedId == Blocks.COBBLE_NETHERRACK.id() && worldObj.rand.nextInt(10) == 0) {
			worldObj.playSoundEffect(null,
				SoundCategory.ENTITY_SOUNDS,
				(float) x + 0.5F,
				(float) y + 0.5F,
				(float) z + 0.5F,
				"mob.ghast.screan",
				2.0F,
				(worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
		}

		if (grindProgress < 200) {
			return;
		}

		grindProgress = 0;

		if (storedId == WolvesBlocks.COMPANION_CUBE.id()) {
			ejectStack(new ItemStack(WolvesItems.FOOD_WOLFCHOP_RAW));
			if(inv[unmilledIndex].getMetadata() == 0) {
				worldObj.playSoundEffect(null,
					SoundCategory.ENTITY_SOUNDS,
					(float) x + 0.5F,
					(float) y + 0.5F,
					(float) z + 0.5F,
					"mob.wolf.whine",
					2.0F,
					(worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F + 1.0F);
			}

			removeItem(unmilledIndex, 1);
		}

		ItemStack milledStack = MillStoneRecipeRegistry.getInstance().getResult(storedId);
		if (milledStack == null) {
			return;
		}

		milledStack = new ItemStack(milledStack.itemID, milledStack.stackSize, milledStack.getMetadata());
		if (milledStack.stackSize == 0) {
			milledStack.stackSize = 1;
		}

		removeItem(unmilledIndex, 1);
		ejectStack(milledStack);
	}

	private void ejectStack(ItemStack stack) {
		if (worldObj == null) {
			return;
		}

		BlockPosition randPos = new BlockPosition(x, y, z);
		int dir = 2 + worldObj.rand.nextInt(4);
		randPos.addOffset(dir);

		worldObj.dropItem(randPos.x, randPos.y, randPos.z, stack);
	}

	public int getUnmilledInventoryIndex() {
		for (int temp = 0; temp < 3; temp++) {
			if (inv[temp] == null) {
				continue;
			}

			Item tempItem = inv[temp].getItem();
			if (tempItem != null) {
				return temp;
			}
		}

		return -1;
	}

	public boolean IsWholeCompanionCubeNextToBeProcessed() {
		int unmilledIndex = getUnmilledInventoryIndex();
		if (unmilledIndex < 0) {
			return false;
		}

		int unmilledId = inv[unmilledIndex].itemID;
		return unmilledId == WolvesBlocks.COMPANION_CUBE.id() && inv[unmilledIndex].getMetadata() == 0;
	}
}
