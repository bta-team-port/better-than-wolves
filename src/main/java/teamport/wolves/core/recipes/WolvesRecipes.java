package teamport.wolves.core.recipes;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import teamport.wolves.core.items.WolvesItems;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.util.RecipeEntrypoint;

import static teamport.wolves.BetterThanWolves.MOD_ID;

public class WolvesRecipes implements RecipeEntrypoint {
	@Override
	public void onRecipesReady() {
		MillStoneRecipeRegistry.getInstance().addRecipe(Items.WHEAT.id, new ItemStack(WolvesItems.FLOUR));
		MillStoneRecipeRegistry.getInstance().addRecipe(Items.LEATHER.id, new ItemStack(WolvesItems.LEATHER_SCOURED));
		MillStoneRecipeRegistry.getInstance().addRecipe(WolvesItems.HEMP.id, new ItemStack(WolvesItems.HEMP_FIBER, 4));
		MillStoneRecipeRegistry.getInstance().addRecipe(Items.SUGARCANE.id, new ItemStack(Items.DUST_SUGAR, 3));
		MillStoneRecipeRegistry.getInstance().addRecipe(Items.COAL.id, new ItemStack(WolvesItems.DUST_COAL));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.NETHERRACK.id(), new ItemStack(WolvesItems.DUST_NETHERRACK));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.COBBLE_NETHERRACK.id(), new ItemStack(WolvesItems.DUST_NETHERRACK));
		MillStoneRecipeRegistry.getInstance().addRecipe(Items.BONE.id, new ItemStack(Items.DYE, 3, 15));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.FLOWER_LIGHT_BLUE.id(), new ItemStack(Items.DYE, 2, 12));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.FLOWER_ORANGE.id(), new ItemStack(Items.DYE, 2, 14));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.FLOWER_PINK.id(), new ItemStack(Items.DYE, 2, 9));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.FLOWER_PURPLE.id(), new ItemStack(Items.DYE, 2, 5));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.FLOWER_RED.id(), new ItemStack(Items.DYE, 2, 1));
		MillStoneRecipeRegistry.getInstance().addRecipe(Blocks.FLOWER_YELLOW.id(), new ItemStack(Items.DYE, 2, 11));
	}

	@Override
	public void initNamespaces() {
		new MillStoneRecipeRegistry();
		RecipeBuilder.getRecipeNamespace(MOD_ID);
	}
}
