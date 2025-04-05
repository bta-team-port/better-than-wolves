package teamport.wolves.core.container;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import teamport.wolves.core.blocks.entity.TileEntityMillStone;

import java.util.ArrayList;
import java.util.List;

public class MenuMillstone extends MenuAbstract {
	TileEntityMillStone tileEntity;
	private int grindProgress = 0;

	public MenuMillstone(ContainerInventory inventory, TileEntityMillStone tileEntity) {
		this.tileEntity = tileEntity;

		for (int millInv = 0; millInv < 3; millInv++) {
			addSlot(new Slot(tileEntity, millInv, 80, 43 + millInv * 18));
		}

		for(int invRows = 0; invRows < 3; ++invRows) {
			for(int invCols = 0; invCols < 9; ++invCols) {
				addSlot(new Slot(inventory, invCols + invRows * 9 + 9, 8 + invCols * 18, 111 + invRows * 18));
			}
		}

		for(int invHotbar = 0; invHotbar < 9; ++invHotbar) {
			this.addSlot(new Slot(inventory, invHotbar, 8 + invHotbar * 18, 169));
		}
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();

		for (ContainerListener crafter : containerListeners) {
			if (grindProgress != tileEntity.grindProgress) {
				crafter.updateCraftingInventoryInfo(this, 0, tileEntity.grindProgress);
			}
		}
	}

	@Override
	public void setData(int id, int value) {
		if (id == 0) {
			tileEntity.grindProgress = value;
		}
	}

	@Override
	public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int i, Player player) {
		if (slot.index >= 0 && slot.index <= 3) {
			return getSlots(slot.index, 1, false);
		} else {
			if (action == InventoryAction.MOVE_ALL) {
				if (slot.index >= 4 && slot.index <= 30) {
					return getSlots(4, 27, false);
				}

				if (slot.index >= 30 && slot.index <= 38) {
					return getSlots(30, 9, false);
				}
			}

			return slot.index >= 4 && slot.index <= 38 ? getSlots(4, 36, false) : null;
		}
	}

	@Override
		public List<Integer> getTargetSlots (InventoryAction action, Slot slot,int target, Player player) {
		if (slot.index >= 4 && slot.index <= 39) {
			if (action != InventoryAction.MOVE_ALL) {
				if (target == 1) {
					return getSlots(0, 1, false);
				}

				if (target == 2) {
					return getSlots(1, 1, false);
				}
			}

			if (slot.index <= 29) {
				return getSlots(30, 9, false);
			}

			if (slot.index >= 31 && slot.index <= 38) {
				return getSlots(3, 27, false);
			}
		}

		if (slot.index >= 0 && slot.index <= 3) {
			return slot.index == 3 ? getSlots(4, 36, true) : getSlots(4, 36, false);
		} else {
			return null;
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return tileEntity.stillValid(player);
	}
}
