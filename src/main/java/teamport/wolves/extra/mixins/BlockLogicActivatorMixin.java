package teamport.wolves.extra.mixins;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicActivator;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.animal.MobWolf;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import teamport.wolves.core.blocks.WolvesBlocks;

import java.util.List;
import java.util.Random;

@Mixin(value = BlockLogicActivator.class, remap = false)
public abstract class BlockLogicActivatorMixin extends BlockLogic {
	public BlockLogicActivatorMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Inject(method = "useItem", at = @At("TAIL"))
	private void wolves_activatorUse(World world, int x, int y, int z, Random random, CallbackInfo ci) {
		@NotNull List<@NotNull MobWolf> mob = world.getEntitiesWithinAABB(MobWolf.class, AABB.getTemporaryBB(x, y, z, x + 2, y + 2, z + 2).grow(2, 2, 2));

		if (!mob.isEmpty()) {
			for (MobWolf wolf : mob) {
				world.dropItem((int) wolf.x, (int) wolf.y, (int) wolf.z, WolvesBlocks.COMPANION_CUBE.getDefaultStack());

				world.playSoundEffect(null,
					SoundCategory.ENTITY_SOUNDS,
					wolf.x,
					wolf.y,
					wolf.z,
					"mob.wolf.whine",
					0.3F,
					(random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);

				world.playSoundEffect(null,
					SoundCategory.WORLD_SOUNDS,
					x,
					y,
					z,
					"tile.activator.use",
					1.0F,
					1.0F);

				wolf.remove();
			}
		}
	}
}
