package teamport.wolves.core.recipes;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.DataLoader;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeGroup;
import net.minecraft.core.data.registry.recipe.RecipeNamespace;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import teamport.wolves.core.WolvesConfig;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.items.WolvesItems;
import teamport.wolves.core.recipes.entry.RecipeEntryCauldron;
import teamport.wolves.core.recipes.entry.RecipeEntryMillstone;
import turniplabs.halplibe.helper.RecipeBuilder;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderBlastFurnace;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderFurnace;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShaped;
import turniplabs.halplibe.helper.recipeBuilders.RecipeBuilderShapeless;
import turniplabs.halplibe.helper.recipeBuilders.modifiers.WorkbenchModifier;
import turniplabs.halplibe.util.RecipeEntrypoint;

import static teamport.wolves.BetterThanWolves.MOD_ID;

// TODO - Replace 'TEMPORARY!' recipes!
public class WolvesRecipes implements RecipeEntrypoint {
	public static RecipeNamespace WOLVES;
	public static RecipeGroup<RecipeEntryMillstone> MILLSTONE;
	public static RecipeGroup<RecipeEntryCauldron> CAULDRON;

	@Override
	public void onRecipesReady() {

		removeRecipes();
		addWorkbenchRecipes();
		addFurnaceRecipes();
		load();
	}


	@Override
	public void initNamespaces() {
		resetGroups();
		registerNamespaces();
	}

	private void resetGroups() {
		WOLVES = new RecipeNamespace();
		MILLSTONE = new RecipeGroup<>(new RecipeSymbol(new ItemStack(WolvesBlocks.MILLSTONE)));
		CAULDRON = new RecipeGroup<>(new RecipeSymbol(new ItemStack(WolvesBlocks.CAULDRON)));

		Registries.RECIPES.unregister(MOD_ID);
	}

	private void registerNamespaces() {
		WOLVES.register("millstone", MILLSTONE);
		WOLVES.register("cauldron", CAULDRON);
		Registries.RECIPES.register(MOD_ID, WOLVES);
	}

	private void load() {
		RecipeBuilder.addItemsToGroup(MOD_ID, "netherracks", Blocks.COBBLE_NETHERRACK, Blocks.NETHERRACK);

		Registries.RECIPE_TYPES.register("wolves:millstone", RecipeEntryMillstone.class);
		Registries.RECIPE_TYPES.register("wolves:cauldron", RecipeEntryCauldron.class);

		DataLoader.loadRecipesFromFile("/assets/wolves/recipes/millstone/millstone.json");

		if (WolvesConfig.cfg.getBoolean("Difficulty.newRopeRecipe")) {
			DataLoader.loadRecipesFromFile("/assets/wolves/recipes/millstone/config/millstone_rope_1.json");
		} else {
			DataLoader.loadRecipesFromFile("/assets/wolves/recipes/millstone/config/millstone_rope_2.json");
		}

		DataLoader.loadRecipesFromFile("/assets/wolves/recipes/cauldron/cauldron.json");
	}

	private void addFurnaceRecipes() {
		if (WolvesConfig.cfg.getBoolean("Difficulty.newFlourRecipes")) {
			new RecipeBuilderFurnace(MOD_ID)
				.setInput(WolvesItems.FLOUR)
				.create("bread", new ItemStack(Items.FOOD_BREAD));
		}

		new RecipeBuilderBlastFurnace(MOD_ID)
			.setInput(WolvesItems.INGOT_SOULFORGED_STEEL_CRUDE)
			.create("crude_soulforged_steel_to_soulforged_steel", new ItemStack(WolvesItems.INGOT_SOULFORGED_STEEL));
	}

	private void addWorkbenchRecipes() {
		new RecipeBuilderShaped(MOD_ID, "121", "232", "444")
			.addInput('1', Items.DUST_SUGAR)
			.addInput('2', Items.BUCKET_MILK)
			.addInput('3', Items.EGG_CHICKEN)
			.addInput('4', WolvesItems.FLOUR)
			.create("vanilla_cake", new ItemStack(WolvesItems.FOOD_CAKE_PLAIN));

		if (WolvesConfig.cfg.getBoolean("Difficulty.newFlourRecipes")) {
			new RecipeBuilderShaped(MOD_ID, "121", "343", "555")
				.addInput('1', Items.DUST_SUGAR)
				.addInput('2', Items.FOOD_CHERRY)
				.addInput('3', Items.BUCKET_MILK)
				.addInput('4', Items.EGG_CHICKEN)
				.addInput('5', WolvesItems.FLOUR)
				.create("cherry_cake", new ItemStack(Items.FOOD_CAKE));

			new RecipeBuilderShapeless(MOD_ID)
				.addInput(new ItemStack(Items.DYE, 1, 3))
				.addInput(Items.DUST_SUGAR)
				.addInput(WolvesItems.FLOUR)
				.create("cookie", new ItemStack(Items.FOOD_COOKIE, 16));

			new RecipeBuilderShaped(MOD_ID, "121", "343", "555")
				.addInput('1', Items.DUST_SUGAR)
				.addInput('2', Items.BUCKET_MILK)
				.addInput('3', Items.EGG_CHICKEN)
				.addInput('4', Blocks.PUMPKIN)
				.addInput('5', WolvesItems.FLOUR)
				.create("pumpkin_pie", new ItemStack(Items.FOOD_PUMPKIN_PIE));
		}

		new RecipeBuilderShaped(MOD_ID, "121", "343", "555")
			.addInput('1', Items.DUST_SUGAR)
			.addInput('2', new ItemStack(Items.DYE, 1, 3))
			.addInput('3', Items.BUCKET_MILK)
			.addInput('4', Items.EGG_CHICKEN)
			.addInput('5', WolvesItems.FLOUR)
			.create("chocolate_cake", new ItemStack(WolvesItems.FOOD_CAKE_CHOCOLATE));

		new RecipeBuilderShaped(MOD_ID, "111", "232", "444")
			.addInput('1', Items.DUST_SUGAR)
			.addInput('2', Items.BUCKET_MILK)
			.addInput('3', Items.EGG_CHICKEN)
			.addInput('4', WolvesItems.FLOUR)
			.create("sprinkled_cake", new ItemStack(WolvesItems.FOOD_CAKE_SPRINKLES));

		// TEMPORARY!! Replace this with a 'Hopper w/ Soul-Sand' recipe!
		// Soul-Sand Hoppers also need to spawn a Ghast after 5-7 dust is put in.
		new RecipeBuilderShaped(MOD_ID, "111", "121", "111")
			.addInput('1', WolvesItems.DUST_NETHERRACK)
			.addInput('2', Blocks.SOULSAND)
			.create("hellfire_ingot", new ItemStack(WolvesItems.DUST_HELLFIRE, 8));

		// TEMPORARY! Replace with a 'Crucible' recipe!
		new RecipeBuilderShapeless(MOD_ID)
			.addInput(WolvesItems.DUST_COAL)
			.addInput(WolvesItems.INGOT_HELLFIRE)
			.addInput(Items.INGOT_IRON)
			.addInput(Items.INGOT_IRON)
			.addInput(Items.INGOT_IRON)
			.create("iron_ingot_to_crude_soulforged_steel", new ItemStack(WolvesItems.INGOT_SOULFORGED_STEEL_CRUDE, 3));

		new RecipeBuilderShapeless(MOD_ID)
			.addInput(WolvesItems.INGOT_SOULFORGED_STEEL)
			.create("soulforged_steel_to_crude_soulforged_steel", new ItemStack(WolvesItems.INGOT_SOULFORGED_STEEL_CRUDE));

		if (WolvesConfig.cfg.getBoolean("Difficulty.newRopeRecipe")) {
			new RecipeBuilderShaped(MOD_ID, "11", "11", "11")
				.addInput('1', WolvesItems.HEMP_FIBER)
				.create("rope", new ItemStack(Items.ROPE));
		}

		new RecipeBuilderShaped(MOD_ID, "1", "2", "1")
			.addInput('1', "minecraft:planks")
			.addInput('2', Items.ROPE)
			.create("axle", new ItemStack(WolvesBlocks.AXLE));

		new RecipeBuilderShaped(MOD_ID, "#1#", "121", "#1#")
			.addInput('1', Items.STICK)
			.addInput('2', "minecraft:planks")
			.create("gears", new ItemStack(WolvesItems.GEAR, 2));

		new RecipeBuilderShaped(MOD_ID, "##1", "#1#", "232")
			.addInput('1', Items.STICK)
			.addInput('2', "minecraft:cobblestones")
			.addInput('3', WolvesItems.GEAR)
			.create("hand_crank", new ItemStack(WolvesBlocks.HAND_CRANK));

		new RecipeBuilderShaped(MOD_ID, "111", "111", "121")
			.addInput('1', "minecraft:stones")
			.addInput('2', WolvesItems.GEAR)
			.create("millstone", new ItemStack(WolvesBlocks.MILLSTONE));

		new RecipeBuilderShaped(MOD_ID, "#1#", "121", "#3#")
			.addInput('1', Blocks.GLASS)
			.addInput('2', Items.DUST_GLOWSTONE)
			.addInput('3', Items.DUST_REDSTONE)
			.create("sun_lamp", WolvesBlocks.SUN_LAMP_IDLE);

		new RecipeBuilderShaped(MOD_ID, "111", "111", "111")
			.addInput('1', WolvesItems.HEMP_FIBER)
			.create("fabric", new ItemStack(WolvesItems.FABRIC));
	}

	private void removeRecipes() {
		WorkbenchModifier workbench = RecipeBuilder.ModifyWorkbench("minecraft");

		if (WolvesConfig.cfg.getBoolean("Difficulty.newFlourRecipes")) {
			workbench.removeRecipe("bread");
			workbench.removeRecipe("cake");
			workbench.removeRecipe("cookie");
			workbench.removeRecipe("pumpkin_pie");
		}

		if (WolvesConfig.cfg.getBoolean("Difficulty.newRopeRecipe")) {
			workbench.removeRecipe("rope");
		}
	}
}
