package teamport.wolves.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.Global;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.weather.WeatherRain;
import net.minecraft.core.world.wind.WindProvider;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.useless.DragonFly;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import teamport.wolves.core.entity.logic.EntityWindmill;

@Environment(EnvType.CLIENT)
public class EntityRendererWindmill extends EntityRenderer<EntityWindmill> {
	private StaticEntityModel[] bladeModels = new StaticEntityModel[4];

	public EntityRendererWindmill() {
		shadowSize = 0.0F;
	}

	@Override
	public void render(@NotNull Tessellator tessellator, @NotNull EntityWindmill entity, double x, double y, double z, float yaw, float partialTick) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		bindTexture("/assets/wolves/textures/entity/windmill/0.png");
		StaticEntityModel axleModel = DragonFly.loadEntityModel("geometry.wolves.windmill_axles", 0.0);
		StaticEntityModel bladeModel1 = DragonFly.loadEntityModel("geometry.wolves.windmill_blade_1", 0.0);
		StaticEntityModel bladeModel2 = DragonFly.loadEntityModel("geometry.wolves.windmill_blade_2", 0.0);
		StaticEntityModel bladeModel3 = DragonFly.loadEntityModel("geometry.wolves.windmill_blade_3", 0.0);
		StaticEntityModel bladeModel4 = DragonFly.loadEntityModel("geometry.wolves.windmill_blade_4", 0.0);
		GL11.glScalef(-0.0625f, 0.0625f, 0.0625f);

		float oldRot = entity.getCurrentRotationSpeed();
		float swayInterpolated = oldRot + (entity.getCurrentRotationSpeed() - oldRot) * partialTick;
		float sway = (float)((double)swayInterpolated / 1.5 % (Math.PI * 2));
		WindProvider theWind = Minecraft.getMinecraft().currentWorld.getWorldType().getWindManager();
		float windDirection = theWind.getWindDirection(Minecraft.getMinecraft().currentWorld, 0.0F, 500.0F, 0.0F) * 360.0F;

		if (entity.world.getCurrentWeather() instanceof WeatherRain) {
			GL11.glRotatef((windDirection + sway) * 2, 1.0F, 0.0F, 0.0F);
		} else {
			GL11.glRotatef(windDirection + sway, 1.0F, 0.0F, 0.0F);
		}

		float brightness = 1.0F;
		if (!LightmapHelper.isLightmapEnabled() && !Global.accessor.isFullbrightEnabled()) {
			brightness = entity.getBrightness(partialTick);
		}

		bladeModels[0] = bladeModel1;
		bladeModels[1] = bladeModel2;
		bladeModels[2] = bladeModel3;
		bladeModels[3] = bladeModel4;
		for (int i = 0; i < 4; i++) {
			int color = entity.getBladeColor(i).blockMeta;
			GL11.glColor3f(brightness * EntityWindmill.BLADE_COLOR_TABLE[color][0], brightness * EntityWindmill.BLADE_COLOR_TABLE[color][1], brightness * EntityWindmill.BLADE_COLOR_TABLE[color][2]);
			bladeModels[i].render(tessellator);
		}

		axleModel.render(tessellator);
		GL11.glPopMatrix();
	}
}
