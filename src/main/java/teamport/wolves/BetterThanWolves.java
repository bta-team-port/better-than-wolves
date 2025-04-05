package teamport.wolves;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import teamport.wolves.core.WolvesConfig;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.blocks.entity.TileEntityMillStone;
import teamport.wolves.core.items.WolvesItems;
import turniplabs.halplibe.helper.EntityHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class BetterThanWolves implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "wolves";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
		new WolvesConfig();

		EntityHelper.createTileEntity(TileEntityMillStone.class, NamespaceID.getPermanent(MOD_ID, "millstone"));

        LOGGER.info("Better Than Wolves has been initialized.");
    }

	@Override
	public void beforeGameStart() {
		new WolvesBlocks();
		new WolvesItems();
	}

	@Override
	public void afterGameStart() {

	}
}
