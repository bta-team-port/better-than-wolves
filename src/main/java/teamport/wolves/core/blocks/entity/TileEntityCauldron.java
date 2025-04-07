package teamport.wolves.core.blocks.entity;

import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.ListTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventorySorter;
import net.minecraft.core.player.inventory.container.Container;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.blocks.logic.BlockLogicCauldron;
import teamport.wolves.core.recipes.WolvesRecipes;
import teamport.wolves.core.recipes.entry.RecipeEntryCauldron;
import teamport.wolves.core.util.InventoryHandler;

public class TileEntityCauldron extends TileEntity implements Container {
	private ItemStack[] inv = new ItemStack[27];
	private int fireFactor;
	private boolean containsValidIngredients;
	public int cookProgress;

	public TileEntityCauldron() {
		fireFactor = 0;
		cookProgress = 0;
		containsValidIngredients = false;
	}

	@Override
	public int getContainerSize() {
		return inv.length;
	}

	@Override
	public @Nullable ItemStack getItem(int i) {
		return inv[i];
	}

	@Override
	public @Nullable ItemStack removeItem(int index, int amount) {
		if (this.inv[index] != null) {
			if (this.inv[index].stackSize <= amount) {
				ItemStack stack = this.inv[index];
				this.inv[index] = null;
				this.setChanged();
				return stack;
			} else {
				ItemStack splitStack = this.inv[index].splitStack(amount);
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

	@Override
	public void setItem(int index, @Nullable ItemStack stack) {
		this.inv[index] = stack;
		if (stack != null && stack.stackSize > this.getMaxStackSize()) {
			stack.stackSize = this.getMaxStackSize();
		}

		this.setChanged();
	}

	@Override
	public String getNameTranslationKey() {
		return "wolves.container.cauldron.name";
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public boolean stillValid(Player player) {
		if (worldObj == null || worldObj.getTileEntity(x, y, z) != this) {
			return false;
		}

		return player.distanceToSqr((double)x + 0.5,
			(double)this.y + 0.5,
			(double)this.z + 0.5) <= 64.0;
	}

	@Override
	public void sortContainer() {
		InventorySorter.sortInventory(inv);
	}

	public void notifyOfFireChange(int state) {
		validateContentsForState(state);
		validateFireFactor(state);
	}

	private void validateFireFactor(int state) {
		int factor = 0;
		if (state > 0) {
			factor = 5;
			int testID = Blocks.FIRE.id();
			int tempY = y - 1;

			for (int tempX = x - 1; tempX <= x + 1; tempX++) {
				for (int tempZ = z - 1; tempZ <= z + 1; tempZ++) {
					if (worldObj.getBlockId(tempX, tempY, tempZ) == testID) {
						factor++;
					}
				}
			}
		}

		this.fireFactor = factor;
	}

	private void validateContentsForState(int state) {
		this.containsValidIngredients = false;

		if (state == 1) {
			for (RecipeEntryCauldron recipe : WolvesRecipes.CAULDRON.getAllRecipes()) {
				if (recipe.matches(this)) {
					this.containsValidIngredients = true;
				}
			}
		}
	}

	@Override
	public void tick() {
		if (worldObj == null || worldObj.isClientSide) {
			return;
		}

		BlockLogicCauldron cauldron = worldObj.getBlockLogic(x, y, z, BlockLogicCauldron.class);
		if (cauldron == null) {
			return;
		}

		int fireUnderState = cauldron.getFireUnderState(worldObj, x, y, z);
		validateFireFactor(fireUnderState);
		validateContentsForState(fireUnderState);

		if (this.containsValidIngredients) {
			performNormalFireUpdate();
		} else {
			cookProgress = 0;
		}
	}

	// TODO - This is incredibly buggy!
	private void performNormalFireUpdate() {
		if (worldObj == null) {
			return;
		}

		if (containsValidIngredients) {
			cookProgress += fireFactor;

			if (cookProgress >= 1950) {
				cookProgress = 0;

				for (RecipeEntryCauldron recipeEntry : WolvesRecipes.CAULDRON.getAllRecipes()) {
					ItemStack result = recipeEntry.getResult(this);

					if (result != null) {
						for (int i = 0; i < inv.length; i++) {
							ItemStack invStack = inv[i];

							for (RecipeSymbol symbol : recipeEntry.getInput()) {
								if (symbol.matches(invStack)) {
									removeItem(i, symbol.getStack().stackSize);

									if (!InventoryHandler.addItemStackToInventory(this, result)) {
										InventoryHandler.dropWithRandomOffset(worldObj, x, y, z, result);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public boolean isCooking() {
		return cookProgress > 0;
	}

	public int getCookProgressScaled(int scale) {
		return (cookProgress * scale) / 1950;
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		super.writeToNBT(tag);
		tag.putInt("FireFactor", this.fireFactor);
		tag.putInt("CookProgress", this.cookProgress);

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
	}

	@Override
	public void readFromNBT(CompoundTag tag) {
		super.readFromNBT(tag);
		this.fireFactor = tag.getInteger("FireFactor");
		this.cookProgress = tag.getInteger("CookProgress");

		ListTag items = tag.getList("Items");
		this.inv = new ItemStack[this.getContainerSize()];

		for(int i = 0; i < items.tagCount(); ++i) {
			CompoundTag invTag = (CompoundTag)items.tagAt(i);
			int j = invTag.getByte("Slot") & 255;
			if (j < this.inv.length) {
				this.inv[j] = ItemStack.readItemStackFromNbt(invTag);
			}
		}
	}
}
