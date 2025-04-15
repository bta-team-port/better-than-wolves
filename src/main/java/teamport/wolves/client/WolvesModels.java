package teamport.wolves.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.*;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.Side;
import org.useless.DragonFly;
import org.useless.dragonfly.models.block.BlockModelDFJava;
import teamport.wolves.client.model.BlockModelGearBox;
import teamport.wolves.client.renderer.EntityRendererWindmill;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.blocks.metastate.AxleStateInterpreter;
import teamport.wolves.core.blocks.metastate.HandCrankStateInterpreter;
import teamport.wolves.core.entity.logic.EntityWindmill;
import teamport.wolves.core.items.WolvesItems;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static teamport.wolves.BetterThanWolves.MOD_ID;

@Environment(EnvType.CLIENT)
public class WolvesModels implements ModelEntrypoint {

	@Override
	public void initBlockModels(BlockModelDispatcher dispatcher) {
		dispatcher.addDispatch(new BlockModelTransparent<>(WolvesBlocks.SUN_LAMP_IDLE, false)
			.onRenderLayer(1)
			.setAllTextures(0, "wolves:block/sunlamp/idle"));

		dispatcher.addDispatch(new BlockModelStandard<>(WolvesBlocks.SUN_LAMP_ACTIVE)
			.setAllTextures(0, "wolves:block/sunlamp/active"));

		dispatcher.addDispatch(new BlockModelHorizontalRotation<>(WolvesBlocks.COMPANION_CUBE)
			.setTex(0, "wolves:block/companion_cube/front", Side.NORTH)
			.setTex(0, "wolves:block/companion_cube/side", Side.EAST, Side.WEST)
			.setTex(0, "wolves:block/companion_cube/back", Side.SOUTH)
			.setTex(0, "wolves:block/companion_cube/bottom", Side.BOTTOM)
			.setTex(0, "wolves:block/companion_cube/top", Side.TOP));

		dispatcher.addDispatch(new BlockModelHorizontalRotation<>(WolvesBlocks.COMPANION_MEAT)
			.setTex(0, "wolves:block/companion_cube/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "wolves:block/companion_cube/bottom", Side.BOTTOM)
			.setTex(0, "wolves:block/companion_cube/slab", Side.TOP));

		dispatcher.addDispatch(new BlockModelSlab<>(WolvesBlocks.COMPANION_SLAB)
			.setTex(0, "wolves:block/companion_cube/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "wolves:block/companion_cube/bottom", Side.BOTTOM)
			.setTex(0, "wolves:block/companion_cube/slab", Side.TOP));

		dispatcher.addDispatch(new BlockModelDFJava<>(WolvesBlocks.HAND_CRANK, DragonFly.loadBlockModel("wolves:blocks/hand_crank_off"))
			.setStateInterpreter(new HandCrankStateInterpreter())
			.setStateData("wolves:blocks/hand_crank"));

		dispatcher.addDispatch(new BlockModelDFJava<>(WolvesBlocks.AXLE, DragonFly.loadBlockModel("wolves:blocks/axle_up"))
			.setStateInterpreter(new AxleStateInterpreter())
			.setStateData("wolves:blocks/axle"));

		dispatcher.addDispatch(new BlockModelStandard<>(WolvesBlocks.MILLSTONE)
			.setTex(0, "wolves:block/millstone/top", Side.TOP)
			.setTex(0, "wolves:block/millstone/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "wolves:block/millstone/bottom", Side.BOTTOM));

		dispatcher.addDispatch(new BlockModelStandard<>(WolvesBlocks.CAULDRON)
			.setTex(0, "wolves:block/cauldron/top", Side.TOP)
			.setTex(0, "wolves:block/cauldron/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "wolves:block/cauldron/bottom", Side.BOTTOM));

		dispatcher.addDispatch(new BlockModelCake<>(WolvesBlocks.CAKE_PLAIN)
			.setTex(0, "wolves:block/cake/top_plain", Side.TOP)
			.setTex(0, "minecraft:block/cake/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "minecraft:block/cake/bottom", Side.BOTTOM));

		dispatcher.addDispatch(new BlockModelCake<>(WolvesBlocks.CAKE_CHOCOLATE)
			.setTex(0, "wolves:block/cake/top_chocolate", Side.TOP)
			.setTex(0, "minecraft:block/cake/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "minecraft:block/cake/bottom", Side.BOTTOM));

		dispatcher.addDispatch(new BlockModelCake<>(WolvesBlocks.CAKE_SPRINKLES)
			.setTex(0, "wolves:block/cake/top_sprinkles", Side.TOP)
			.setTex(0, "minecraft:block/cake/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "minecraft:block/cake/bottom", Side.BOTTOM));

		dispatcher.addDispatch(new BlockModelGearBox<>(WolvesBlocks.GEAR_BOX));
	}

	@Override
	public void initItemModels(ItemModelDispatcher dispatcher) {
		ModelHelper.setItemModel(WolvesItems.GEAR, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.GEAR, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/gear");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.HEMP, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.HEMP, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/hemp/hemp");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.HEMP_FIBER, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.HEMP_FIBER, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/hemp/fiber");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FABRIC, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FABRIC, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/hemp/fabric");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_WOLFCHOP_RAW, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_WOLFCHOP_RAW, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("minecraft:item/food_porkchop_raw");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_WOLFCHOP_COOKED, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_WOLFCHOP_COOKED, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("minecraft:item/food_porkchop_cooked");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FLOUR, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FLOUR, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/flour");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.LEATHER_SCOURED, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.LEATHER_SCOURED, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/leather/scoured");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.LEATHER_TANNED, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.LEATHER_TANNED, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/leather/tanned");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.DUST_COAL, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.DUST_COAL, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/dust/dust_coal");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.DUST_NETHERRACK, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.DUST_NETHERRACK, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/dust/dust_netherrack");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.DUST_HELLFIRE, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.DUST_HELLFIRE, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/dust/dust_hellfire");
			itemModel.setFullBright();
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.INGOT_HELLFIRE, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.INGOT_HELLFIRE, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/ingot/hellfire");
			itemModel.setFullBright();
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_DONUT, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_DONUT, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/donut/plain");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_DONUT_CHOCOLATE, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_DONUT_CHOCOLATE, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/donut/chocolate");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_DONUT_GLAZED, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_DONUT_GLAZED, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/donut/glazed");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_DONUT_FILLED, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_DONUT_FILLED, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/donut/filled");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_CAKE_PLAIN, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_CAKE_PLAIN, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/cake/plain");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_CAKE_CHOCOLATE, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_CAKE_CHOCOLATE, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/cake/chocolate");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.FOOD_CAKE_SPRINKLES, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.FOOD_CAKE_SPRINKLES, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/food/cake/sprinkles");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.INGOT_SOULFORGED_STEEL, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.INGOT_SOULFORGED_STEEL, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/ingot/soulforged_steel");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.INGOT_SOULFORGED_STEEL_CRUDE, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.INGOT_SOULFORGED_STEEL_CRUDE, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/ingot/soulforged_steel_crude");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.DUNG, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.DUNG, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/dung");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.SAIL, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.SAIL, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/windmill/sail");
			return itemModel;
		});

		ModelHelper.setItemModel(WolvesItems.WINDMILL, () -> {
			ItemModelStandard itemModel = new ItemModelStandard(WolvesItems.WINDMILL, MOD_ID);
			itemModel.icon = TextureRegistry.getTexture("wolves:item/windmill/windmill");
			return itemModel;
		});
	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {
		ModelHelper.setEntityModel(EntityWindmill.class, EntityRendererWindmill::new);
	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
