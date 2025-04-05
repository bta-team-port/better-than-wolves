package teamport.wolves.core.items;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import teamport.wolves.core.WolvesConfig;
import turniplabs.halplibe.helper.ItemBuilder;

import static teamport.wolves.BetterThanWolves.MOD_ID;

public class WolvesItems {
	private static int itemID = WolvesConfig.cfg.getInt("IDs.itemIDs");

	private static int nextID() {
		return itemID++;
	}

	public static final Item GEAR;
	public static final Item HEMP;
	public static final Item HEMP_FIBER;
	public static final Item FOOD_WOLFCHOP_RAW;
	public static final Item FOOD_WOLFCHOP_COOKED;
	public static final Item FLOUR;
	public static final Item LEATHER_SCOURED;
//	public static final Item LEATHER_TANNED;
	public static final Item DUST_COAL;
	public static final Item DUST_NETHERRACK;
	public static final Item DUST_HELLFIRE;

	static {
		GEAR = new ItemBuilder(MOD_ID)
			.build(new Item("gear", "wolves:item/gear", nextID()));

		HEMP = new ItemBuilder(MOD_ID)
			.build(new Item("hemp", "wolves:item/hemp/hemp", nextID()));

		HEMP_FIBER = new ItemBuilder(MOD_ID)
			.build(new Item("hempfiber", "wolves:item/hemp/fiber", nextID()));

		FOOD_WOLFCHOP_RAW = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.wolfchop.raw", "wolves:item/food_wolfchop_raw", nextID(), 3, 16, true, 4));

		FOOD_WOLFCHOP_COOKED = new ItemBuilder(MOD_ID)
			.build(new ItemFood("food.wolfchop.cooked", "wolves:item/food_wolfchop_cooked", nextID(), 8, 16, true, 4));

		FLOUR = new ItemBuilder(MOD_ID)
			.build(new Item("flour", "wolves:item/flour", nextID()));

		LEATHER_SCOURED = new ItemBuilder(MOD_ID)
			.build(new Item("leather.scoured", "wolves:item/leather/scoured", nextID()));

		DUST_COAL = new ItemBuilder(MOD_ID)
			.build(new Item("dust.coal", "wolves:item/dust/dust_coal", nextID()));

		DUST_NETHERRACK = new ItemBuilder(MOD_ID)
			.build(new Item("dust.netherrack", "wolves:item/dust/dust_netherrack", nextID()));

		DUST_HELLFIRE = new ItemBuilder(MOD_ID)
			.build(new Item("dust.hellfire", "wolves:item/dust/dust_hellfire", nextID()));
	}
}
