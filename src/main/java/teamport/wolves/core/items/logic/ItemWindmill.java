package teamport.wolves.core.items.logic;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.entity.logic.EntityWindmill;

public class ItemWindmill extends Item {
	public ItemWindmill(String translationKey, String namespaceId, int id) {
		super(translationKey, namespaceId, id);
	}

	@Override
	public boolean onUseItemOnBlock(ItemStack stack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
		int targetBlock = world.getBlockId(blockX, blockY, blockZ);
		if (!world.isClientSide) {
			if (targetBlock == WolvesBlocks.AXLE.id()) {
				int meta = world.getBlockMetadata(blockX, blockY, blockZ);
				boolean horizontal = (meta & 0b0000_0011) != 0;

				if (horizontal) {
					Axis axis = side.getAxis();
					boolean aligned = axis == Axis.X;

					if (EntityWindmill.validateArea(world, blockX, blockY, blockZ, aligned)) {
						EntityWindmill windmill = new EntityWindmill(world);
						windmill.setPos(blockX + 0.5, blockY + 0.5, blockZ + 0.5);
						world.entityJoinedWorld(windmill);
						windmill.setAligned(aligned);
						stack.consumeItem(player);

						return true;
					} else {
						player.sendMessage("There's not enough room to place a windmill!");
					}
				} else {
					player.sendMessage("The axle must be horizontal!");
				}
			} else {
				player.sendMessage("Windmill must be placed on an Axle!");
			}
		}

		return false;
	}
}
