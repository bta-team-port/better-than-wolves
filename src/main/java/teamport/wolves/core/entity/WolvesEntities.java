package teamport.wolves.core.entity;

import net.minecraft.core.util.collection.NamespaceID;
import teamport.wolves.core.entity.logic.EntityWindmill;
import turniplabs.halplibe.helper.EntityHelper;

import static teamport.wolves.BetterThanWolves.MOD_ID;

public class WolvesEntities {


	static {
		EntityHelper.createEntity(EntityWindmill.class, NamespaceID.getTemp(MOD_ID, "windmill"), "Windmill");
	}
}
