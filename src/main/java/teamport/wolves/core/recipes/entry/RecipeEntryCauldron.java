package teamport.wolves.core.recipes.entry;

import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import teamport.wolves.core.recipes.adapter.RecipeCauldronJsonAdapter;
import teamport.wolves.core.util.InventoryHandler;

import java.util.List;

public class RecipeEntryCauldron extends RecipeEntryBase<List<RecipeSymbol>, ItemStack, Void> implements HasJsonAdapter {

	public RecipeEntryCauldron(List<RecipeSymbol> inputs, ItemStack output) {
		super(inputs, output, null);
	}

	public RecipeEntryCauldron() {
	}

	@Override
	public RecipeJsonAdapter<?> getAdapter() {
		return new RecipeCauldronJsonAdapter();
	}

	public boolean matches(Container inventory) {
		if (getInput() != null || !getInput().isEmpty()) {
			for (int index = 0; index < getInput().size(); index++) {
				ItemStack tempStack = getInput().get(index).copy().getStack();

				if (tempStack == null) {
					return false;
				}

				if (InventoryHandler.itemCountInInventory(inventory, tempStack.itemID, tempStack.getMetadata()) < tempStack.stackSize) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	public ItemStack getResult(Container inventory) {
		for (int i = 0; i < getInput().size(); i++) {
			if (matches(inventory)) {
				return getOutput().copy();
			}
		}

		return null;
	}
}
