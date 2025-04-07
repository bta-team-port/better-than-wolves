package teamport.wolves.core.items;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemPlaceable;
import teamport.wolves.core.WolvesConfig;
import teamport.wolves.core.blocks.WolvesBlocks;
import turniplabs.halplibe.helper.ItemBuilder;

import static teamport.wolves.BetterThanWolves.MOD_ID;

// TODO - Unique Wolfchop sprites
public class WolvesItems {
	private static int itemID = WolvesConfig.cfg.getInt("IDs.itemIDs");

	private static int nextID() {
		return itemID++;
	}

	public static final Item GEAR;
	public static final Item HEMP;
	public static final Item HEMP_FIBER;
	public static final Item FABRIC;
	public static final Item FOOD_WOLFCHOP_RAW;
	public static final Item FOOD_WOLFCHOP_COOKED;
	public static final Item FOOD_DONUT;
	public static final Item FOOD_DONUT_CHOCOLATE;
	public static final Item FOOD_DONUT_GLAZED;
	public static final Item FOOD_DONUT_FILLED;
	public static final Item FOOD_CAKE_PLAIN;
	public static final Item FOOD_CAKE_CHOCOLATE;
	public static final Item FOOD_CAKE_SPRINKLES;
	public static final Item FLOUR;
	public static final Item LEATHER_SCOURED;
	public static final Item LEATHER_TANNED;
	public static final Item DUST_COAL;
	public static final Item DUST_NETHERRACK;
	public static final Item DUST_HELLFIRE;
	public static final Item INGOT_HELLFIRE;
	public static final Item INGOT_SOULFORGED_STEEL_CRUDE;
	public static final Item INGOT_SOULFORGED_STEEL;
	public static final Item DUNG;

	static {
		GEAR = new ItemBuilder(MOD_ID)
			.build(new Item("gear", "wolves:item/gear", nextID()));

		HEMP = new ItemBuilder(MOD_ID)
			.build(new Item("hemp", "wolves:item/hemp", nextID()));

		HEMP_FIBER = new ItemBuilder(MOD_ID)
			.build(new Item("hempfiber", "wolves:item/hemp_fiber", nextID()));

		FABRIC = new ItemBuilder(MOD_ID)
			.build(new Item("fabric", "wolves:item/fabric", nextID()));

		FOOD_WOLFCHOP_RAW = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.wolfchop.raw", "wolves:item/food_wolfchop_raw", nextID(), 3, 16, true, 4));

		FOOD_WOLFCHOP_COOKED = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.wolfchop.cooked", "wolves:item/food_wolfchop_cooked", nextID(), 8, 16, true, 4));

		FOOD_DONUT = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.donut", "wolves:item/food_donut", nextID(), 1, 8, false, 12));

		FOOD_DONUT_CHOCOLATE = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.donut.chocolate", "wolves:item/food_donut_chocolate", nextID(), 1, 6, false, 12));

		FOOD_DONUT_GLAZED = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.donut.glazed", "wolves:item/food_donut_glazed", nextID(), 1, 6, false, 12));

		FOOD_DONUT_FILLED = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.donut.filled", "wolves:item/food_donut_filled", nextID(), 1, 6, false, 12));

		FOOD_CAKE_PLAIN = new ItemBuilder(MOD_ID)
			.build(new ItemPlaceable("food.cake.plain", "wolves:item/food_cake_plain", nextID(), WolvesBlocks.CAKE_PLAIN));

		FOOD_CAKE_CHOCOLATE = new ItemBuilder(MOD_ID)
			.build(new ItemPlaceable("food.cake.chocolate", "wolves:item/food_cake_chocolate", nextID(), WolvesBlocks.CAKE_CHOCOLATE));

		FOOD_CAKE_SPRINKLES = new ItemBuilder(MOD_ID)
			.build(new ItemPlaceable("food.cake.sprinkles", "wolves:item/food_cake_sprinkles", nextID(), WolvesBlocks.CAKE_SPRINKLES));

		FLOUR = new ItemBuilder(MOD_ID)
			.build(new Item("flour", "wolves:item/flour", nextID()));

		LEATHER_SCOURED = new ItemBuilder(MOD_ID)
			.build(new Item("leather.scoured", "wolves:item/leather_scoured", nextID()));

		LEATHER_TANNED = new ItemBuilder(MOD_ID)
			.build(new Item("leather.tanned", "wolves:item/leather_tanned", nextID()));

		DUST_COAL = new ItemBuilder(MOD_ID)
			.build(new Item("dust.coal", "wolves:item/dust_coal", nextID()));

		DUST_NETHERRACK = new ItemBuilder(MOD_ID)
			.build(new Item("dust.netherrack", "wolves:item/dust_netherrack", nextID()));

		DUST_HELLFIRE = new ItemBuilder(MOD_ID)
			.build(new Item("dust.hellfire", "wolves:item/dust_hellfire", nextID()));

		INGOT_HELLFIRE = new ItemBuilder(MOD_ID)
			.build(new Item("ingot.hellfire", "wolves:item/ingot_hellfire", nextID()));

		INGOT_SOULFORGED_STEEL = new ItemBuilder(MOD_ID)
			.build(new Item("ingot.soulforgedsteel", "wolves:item/ingot_soulforged_steel", nextID()));

		INGOT_SOULFORGED_STEEL_CRUDE = new ItemBuilder(MOD_ID)
			.build(new Item("ingot.soulforgedsteel.crude", "wolves:item/ingot_soulforged_steel_crude", nextID()));

		DUNG = new ItemBuilder(MOD_ID)
			.build(new Item("dung", "wolves:item/dung",nextID()));
	}
}
