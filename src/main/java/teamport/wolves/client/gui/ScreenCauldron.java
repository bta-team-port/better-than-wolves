package teamport.wolves.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import teamport.wolves.core.blocks.entity.TileEntityCauldron;
import teamport.wolves.core.container.MenuCauldron;

@Environment(EnvType.CLIENT)
public class ScreenCauldron extends ScreenContainerAbstract {
	TileEntityCauldron tileEntity;
	public ScreenCauldron(ContainerInventory inventory, TileEntityCauldron tileEntity) {
		super(new MenuCauldron(inventory, tileEntity));
		this.tileEntity = tileEntity;
		this.ySize = 193;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		Texture bg = mc.textureManager.loadTexture("/assets/wolves/textures/gui/cauldron.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.textureManager.bindTexture(bg);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (tileEntity.isCooking()) {
			int progress = tileEntity.getCookProgressScaled(12);
			drawTexturedModalRect(x + 81, (y + 19 + 12) - progress, 176, 12 - progress, 14, progress + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		I18n i18n = I18n.getInstance();
		font.drawString(i18n.translateKey("wolves.gui.cauldron.label.cauldron"), 67, 6, 0x404040);
		font.drawString(i18n.translateKey("wolves.gui.cauldron.label.inventory"), 8, ySize - 96 + 2, 0x404040);
	}
}
