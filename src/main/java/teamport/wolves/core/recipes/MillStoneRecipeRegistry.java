package teamport.wolves.core.recipes;

import net.minecraft.core.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MillStoneRecipeRegistry {
	private static final MillStoneRecipeRegistry INSTANCE = new MillStoneRecipeRegistry();
	private static final Map<Integer, ItemStack> recipes = new HashMap<>();

	public static MillStoneRecipeRegistry getInstance() {
		return INSTANCE;
	}

	public void addRecipe(int input, ItemStack result) {
		recipes.put(input, result);
	}

	public ItemStack getResult(int input) {
		return recipes.get(input);
	}
}
