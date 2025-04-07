package teamport.wolves.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import teamport.wolves.core.blocks.entity.TileEntityMillstone;
import teamport.wolves.core.container.MenuMillstone;

@Environment(EnvType.CLIENT)
public class ScreenMillstone extends ScreenContainerAbstract {
	TileEntityMillstone tileEntity;

	public ScreenMillstone(ContainerInventory inventory, TileEntityMillstone tilEntity) {
		super(new MenuMillstone(inventory, tilEntity));
		this.tileEntity = tilEntity;
		this.ySize = 193;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f) {
		Texture bg = mc.textureManager.loadTexture("/assets/wolves/textures/gui/millstone.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.textureManager.bindTexture(bg);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if (tileEntity.isGrinding()) {
			int progress = tileEntity.getGrindProgressScaled(12);
			drawTexturedModalRect(x + 80, (y + 18 + 12) - progress, 176, 12 - progress, 14, progress + 2);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer() {
		I18n i18n = I18n.getInstance();
		font.drawString(i18n.translateKey("wolves.gui.millstone.label.millstone"), 69, 6, 0x404040);
		font.drawString(i18n.translateKey("wolves.gui.millstone.label.inventory"), 8, ySize - 96 + 2, 0x404040);
	}
}
