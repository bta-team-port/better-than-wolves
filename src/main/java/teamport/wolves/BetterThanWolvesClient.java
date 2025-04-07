package teamport.wolves;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mp.MpGuiEntryClient;
import teamport.wolves.client.WolvesModels;
import teamport.wolves.client.gui.ScreenCauldron;
import teamport.wolves.client.gui.ScreenMillstone;
import teamport.wolves.core.blocks.entity.TileEntityCauldron;
import teamport.wolves.core.blocks.entity.TileEntityMillstone;
import teamport.wolves.core.container.MenuCauldron;
import teamport.wolves.core.container.MenuMillstone;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import java.io.IOException;
import java.net.URISyntaxException;

import static teamport.wolves.BetterThanWolves.LOGGER;
import static teamport.wolves.BetterThanWolves.MOD_ID;

public class BetterThanWolvesClient implements ClientModInitializer, ClientStartEntrypoint {
	@Override
	public void onInitializeClient() {
		Catalyst.GUIS.register("wolves:gui/millstone",
			new MpGuiEntryClient(TileEntityMillstone.class, ScreenMillstone.class, MenuMillstone.class));

		Catalyst.GUIS.register("wolves:gui/cauldron",
			new MpGuiEntryClient(TileEntityCauldron.class, ScreenCauldron.class, MenuCauldron.class));

		try {
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.blockAtlas, true);
			TextureRegistry.initializeAllFiles(MOD_ID, TextureRegistry.itemAtlas, true);
		} catch (URISyntaxException | IOException e) {
			LOGGER.error("Failed to fully initialize assets, some issues may occur!", e);
		}
	}

	@Override
	public void beforeClientStart() {
	}

	@Override
	public void afterClientStart() {
		new WolvesModels();
	}
}
