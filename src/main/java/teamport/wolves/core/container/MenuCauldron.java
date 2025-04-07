package teamport.wolves.core.container;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import teamport.wolves.core.blocks.entity.TileEntityCauldron;

import java.util.List;

public class MenuCauldron extends MenuAbstract {
	private final TileEntityCauldron tileEntity;

	public MenuCauldron(ContainerInventory inventory, TileEntityCauldron tileEntity) {
		this.tileEntity = tileEntity;

		// Tile Inventory
		for (int tileRows = 0; tileRows < 3; tileRows++) {
			for (int tileCols = 0; tileCols < 9; tileCols++) {
				addSlot(new Slot(tileEntity, tileCols + tileRows * 9, 8 + tileCols * 18, 43 + tileRows * 18));
			}
		}

		// Player Inventory
		for(int invRows = 0; invRows < 3; ++invRows) {
			for(int invCols = 0; invCols < 9; ++invCols) {
				addSlot(new Slot(inventory, invCols + invRows * 9 + 9, 8 + invCols * 18, 111 + invRows * 18));
			}
		}

		// Player Hotbar
		for(int invHotbar = 0; invHotbar < 9; ++invHotbar) {
			this.addSlot(new Slot(inventory, invHotbar, 8 + invHotbar * 18, 169));
		}
	}

	@Override
	public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int i, Player player) {
		if (slot.index >= 0 && slot.index <= 26) {
			return getSlots(0, 27, false);
		} else {
			if (action == InventoryAction.MOVE_ALL) {
				if (slot.index >= 27 && slot.index <= 53) {
					return getSlots(27, 27, false);
				}

				if (slot.index >= 54 && slot.index <= 63) {
					return getSlots(54, 9, false);
				}
			}

			return action == InventoryAction.MOVE_SIMILAR && slot.index >= 27 && slot.index <= 63 ? this.getSlots(28, 36, false) : null;
		}
	}

	@Override
	public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, Player player) {
		if (slot.index >= 0 && slot.index <= 26) {
			return this.getSlots(27, 36, false);
		} else {
			if (slot.index >= 27 && slot.index <= 63) {
				if (slot.index <= 43) {
					return getSlots(0, 27, false);
				}

				return getSlots(27, 27, false);
			}
		}

		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return tileEntity.stillValid(player);
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();

		for (ContainerListener crafter : containerListeners) {
			int cookProgress = 0;
			if (cookProgress != tileEntity.cookProgress) {
				crafter.updateCraftingInventoryInfo(this, 0, tileEntity.cookProgress);
			}
		}
	}

	@Override
	public void setData(int id, int value) {
		if (id == 0) {
			tileEntity.cookProgress = value;
		}
	}
}
