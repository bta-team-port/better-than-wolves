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
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.blocks.metastate.AxleStateInterpreter;
import teamport.wolves.core.blocks.metastate.HandCrankStateInterpreter;
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

		dispatcher.addDispatch(new BlockModelStandard<>(WolvesBlocks.MILL_STONE)
			.setTex(0, "wolves:block/mill_stone/top", Side.TOP)
			.setTex(0, "wolves:block/mill_stone/side", Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST)
			.setTex(0, "wolves:block/mill_stone/bottom", Side.BOTTOM));
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
			return itemModel;
		});
	}

	@Override
	public void initEntityModels(EntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {

	}

	@Override
	public void initBlockColors(BlockColorDispatcher dispatcher) {

	}
}
