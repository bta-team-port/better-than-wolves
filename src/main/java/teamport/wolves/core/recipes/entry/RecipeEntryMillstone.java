package teamport.wolves.core.recipes.entry;

import net.minecraft.core.data.registry.recipe.HasJsonAdapter;
import net.minecraft.core.data.registry.recipe.RecipeEntryBase;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.adapter.RecipeJsonAdapter;
import net.minecraft.core.item.ItemStack;
import teamport.wolves.core.recipes.adapter.RecipeMillstoneJsonAdapter;

public class RecipeEntryMillstone extends RecipeEntryBase<RecipeSymbol, ItemStack, Void> implements HasJsonAdapter {

	public RecipeEntryMillstone(RecipeSymbol input, ItemStack output) {
		super(input, output, null);
	}

	public RecipeEntryMillstone() {
	}

	@Override
	public RecipeJsonAdapter<?> getAdapter() {
		return new RecipeMillstoneJsonAdapter();
	}

	public boolean matches(ItemStack stack) {
		return getInput().matches(stack);
	}
}
