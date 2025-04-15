package teamport.wolves.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Direction;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.blocks.logic.BlockLogicAxle;
import teamport.wolves.core.blocks.logic.BlockLogicGearBox;

@Environment(EnvType.CLIENT)
public class BlockModelGearBox<T extends BlockLogicGearBox> extends BlockModelStandard<T> {
	private final IconCoordinate faceTexture;
	private final IconCoordinate sideTexture;
	private final IconCoordinate connectedTexture;

	public BlockModelGearBox(Block<T> block) {
		super(block);
		this.faceTexture = TextureRegistry.getTexture("wolves:block/gear_box/front");
		this.sideTexture = TextureRegistry.getTexture("minecraft:block/planks/oak");
		this.connectedTexture = TextureRegistry.getTexture("wolves:block/gear_box/connected");
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		Direction facing = block.getLogic().getDirection(meta);

		for (int i = 0; i <= 5; i++) {
			Direction direction = Direction.getDirectionById(i);
			if (direction == facing) {
				setTex(0, faceTexture, direction.getSide());
			} else {
				BlockLogicAxle axle = WolvesBlocks.AXLE.getLogic();
				if (renderBlocks.blockAccess.getBlockId(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ()) == WolvesBlocks.AXLE.id() && axle.isAxleOriented(renderBlocks.blockAccess, x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ(), direction.getAxis())) {
					setTex(0, connectedTexture, direction.getSide());
				} else {
					setTex(0, sideTexture, direction.getSide());
				}
			}
		}

		return super.render(tessellator, x, y, z);
	}
}
