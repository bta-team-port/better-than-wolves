package teamport.wolves.core.entity.logic;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.weather.WeatherRain;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import teamport.wolves.core.blocks.WolvesBlocks;
import teamport.wolves.core.items.WolvesItems;

public class EntityWindmill extends Entity {
	private int fullUpdateTickCount;
	private int currentBladeForColoring = 0;
	private float currentRotationSpeed;
	public static final float[][] BLADE_COLOR_TABLE = new float[][]{{1.0F, 1.0F, 1.0F}, {0.95F, 0.7F, 0.2F}, {0.9F, 0.5F, 0.85F}, {0.6F, 0.7F, 0.95F}, {0.9F, 0.9F, 0.2F}, {0.5F, 0.8F, 0.1F}, {0.95F, 0.7F, 0.8F}, {0.3F, 0.3F, 0.3F}, {0.6F, 0.6F, 0.6F}, {0.3F, 0.6F, 0.7F}, {0.7F, 0.4F, 0.9F}, {0.2F, 0.4F, 0.8F}, {0.5F, 0.4F, 0.3F}, {0.4F, 0.5F, 0.2F}, {0.8F, 0.3F, 0.3F}, {0.1F, 0.1F, 0.1F}};
	public static final int DATA_ALIGNED = 16;
	public static final int DATA_ROTATION = 17;
	public static final int DATA_PROVIDING_POWER = 18;
	public static final int DATA_OVERPOWER = 19;
	public static final int DATA_AXIS = 20;
	public static final int DATA_BLADE_COLOR = 21;
	public static final int MASK_ALIGNED = 0b0000_0001;
	public static final int MASK_ROTATION = 0b0000_0010;
	public static final int MASK_OVERPOWER = 0b0000_0100;
	public static final int MASK_AXIS = 0b0000_1000;
	public static final int MASK_BLADE_COLOR_ONE = 0b0001_0000;
	public static final int MASK_BLADE_COLOR_TWO = 0b0010_0000;
	public static final int MASK_BLADE_COLOR_THREE = 0b0100_0000;
	public static final int MASK_BLADE_COLOR_FOUR = 0b1000_0000;

	public EntityWindmill(@Nullable World world) {
		super(world);
		fullUpdateTickCount = 0;
		currentRotationSpeed = 0;
	}

	@Override
	protected void defineSynchedData() {
		entityData.define(DATA_ALIGNED, (byte) 0, Byte.class);
		entityData.define(DATA_ROTATION,  (byte) 0, Byte.class);
		entityData.define(DATA_PROVIDING_POWER, (byte) 0, Byte.class);
		entityData.define(DATA_OVERPOWER, (byte) 0, Byte.class);
		entityData.define(DATA_AXIS, (byte) 0, Byte.class);
		entityData.define(DATA_BLADE_COLOR, (byte) 0, Byte.class);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

	}

	public static boolean validateArea(World world, int x, int y, int z, boolean aligned) {
		if (y + 6 >= world.getHeightBlocks()) {
			return false;
		}

		int xOffset;
		int zOffset;
		if (aligned) {
			xOffset = 0;
			zOffset = 1;
		} else {
			xOffset = 1;
			zOffset = 0;
		}

		for(int heightOffset = -6; heightOffset <= 6; heightOffset++) {
			for (int widthOffset = -6; widthOffset <= 6; widthOffset++) {
				for (int frontOffset = 0; frontOffset <= 1; frontOffset++) {
					if (heightOffset == 0 && widthOffset == 0) {
						continue;
					}

					int tempX = x + xOffset * widthOffset;
					int tempY = y + heightOffset;
					int tempZ = z + zOffset * widthOffset;

					if (!isSuitableBlock(world, tempX, tempY, tempZ)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private static boolean isSuitableBlock(World world, int x, int y, int z) {
		return world.isAirBlock(x, y, z);
	}

	@Override
	public void tick() {
		if (world == null || world.isClientSide) {
			return;
		}

		if (fullUpdateTickCount-- <= 0) {
			fullUpdateTickCount = 20;

			setCurrentRotationSpeed(computeRotation());

			if(!getProvidingPower() && WolvesBlocks.AXLE.getLogic().getPowerLevel(world, MathHelper.floor(x + 1), MathHelper.floor(y), MathHelper.floor(z + 1)) > 0) {
				destroyWithDrop(true);
				return;
			}

			if (getCurrentRotationSpeed() > 0.1F || getCurrentRotationSpeed() < -0.1F) {
				if (!getProvidingPower()) {
					setProvidingPower(true);
					WolvesBlocks.AXLE.getLogic().setPowerLevel(world, MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), 3);
				}
			} else if (getProvidingPower()) {
				setProvidingPower(false);
				WolvesBlocks.AXLE.getLogic().setPowerLevel(world, MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), 0);
			}


			if (world.getBlockId(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z)) != WolvesBlocks.AXLE.id()) {
				System.out.println(MathHelper.floor(x));
				WolvesBlocks.AXLE.getLogic().setPowerLevel(world, MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), 0);
				destroyWithDrop(true);
				return;
			}
		}

		setRotation(getRotation() + currentRotationSpeed);

		if (getRotation() > 360) {
			setRotation(getRotation() - 360);
		} else if (getRotation() < -360) {
			setRotation(getRotation() + 360);
		}
	}

	private float computeRotation() {
		float rotAmount = world.worldType.getWindManager().getWindIntensity(world, 0.0F, 500.0F, 0.0F);
		if (world.canBlockBeRainedOn(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z)) && world.getCurrentWeather() instanceof WeatherRain) {
			if (getOverpower() < 0) {
				setOverpowered(30);
			}
		} else {
			setOverpowered(-1);
		}

		return rotAmount;
	}

	public void destroyWithDrop(boolean dropItem) {
		WolvesBlocks.AXLE.getLogic().setPowerLevel(world, MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z), 0);

		if (dropItem) {
			dropItem(WolvesItems.WINDMILL.id, 1);
		}

		remove();
	}

	public void setProvidingPower(boolean flag) {
		byte data = getEntityData().getByte(DATA_PROVIDING_POWER);
		entityData.set(data, flag ? (byte) 1 : (byte) 0);
	}

	public boolean getProvidingPower() {
		return getEntityData().getByte(DATA_PROVIDING_POWER) != 0;
	}

	public void setAligned(boolean flag) {
		byte data = getEntityData().getByte(DATA_ALIGNED);
		if (flag) {
			entityData.set(DATA_ALIGNED, (byte) (data | MASK_ALIGNED));
		} else {
			entityData.set(DATA_ALIGNED, (byte) (data & ~MASK_ALIGNED));
		}
	}

	public boolean getAligned() {
		return (getEntityData().getByte(DATA_ALIGNED) & MASK_ALIGNED) != 0;
	}

	public void setRotation(float age) {
		int data = getEntityData().getByte(DATA_ROTATION);
		entityData.set(DATA_ROTATION, (byte) (Float.floatToIntBits(age) | MASK_ROTATION));
	}

	public float getRotation() {
		return Float.intBitsToFloat(getEntityData().getByte(DATA_ROTATION) & MASK_ROTATION);
	}

	public void setOverpowered(int timer) {
		int data = getEntityData().getByte(DATA_OVERPOWER);
		entityData.set(data, (byte) (timer | MASK_OVERPOWER));
	}

	public int getOverpower() {
		return (getEntityData().getByte(DATA_OVERPOWER) & MASK_OVERPOWER);
	}

	@Override
	public boolean interact(@NotNull Player player) {
		ItemStack stack = player.inventory.getCurrentItem();

		if (stack != null) {
			if (stack.itemID == Items.DYE.id || stack.itemID == WolvesItems.DUNG.id) {
				@NotNull DyeColor color;

				if (stack.itemID == Items.DYE.id) {
					color = DyeColor.colorFromItemMeta(stack.getMetadata());
				} else {
					color = DyeColor.BROWN;
				}

				setBladeColor(color, currentBladeForColoring);
				currentBladeForColoring++;

				if (currentBladeForColoring >= 4) {
					currentBladeForColoring = 0;
				}

				stack.consumeItem(player);
			}
		}

		return player.distanceToSqr(this) < 256;
	}

	@Override
	public boolean isPickable() {
		return !removed;
	}

	@Override
	public boolean canInteract() {
		return !removed;
	}

	@Override
	protected void causeFallDamage(float distance) {
	}

	@Override
	public boolean hurt(Entity attacker, int baseDamage, DamageType type) {
		destroyWithDrop(attacker instanceof Player && ((Player) attacker).gamemode.dropBlockOnBreak());
		return super.hurt(attacker, baseDamage, type);
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	public float getCurrentRotationSpeed() {
		return currentRotationSpeed;
	}

	public void setCurrentRotationSpeed(float newRotSpeed) {
		this.currentRotationSpeed = newRotSpeed;
	}

	public void setBladeColor(DyeColor color, int blade) {
		switch (blade) {
			case 3:
				entityData.set((byte) DATA_BLADE_COLOR, color.blockMeta & MASK_BLADE_COLOR_FOUR);
			case 2:
				entityData.set((byte) DATA_BLADE_COLOR, color.blockMeta & MASK_BLADE_COLOR_THREE);
			case 1:
				entityData.set((byte) DATA_BLADE_COLOR, color.blockMeta & MASK_BLADE_COLOR_TWO);
			default:
				entityData.set((byte) DATA_BLADE_COLOR, color.blockMeta & MASK_BLADE_COLOR_ONE);
		}
	}

	public DyeColor getBladeColor(int blade) {
		switch (blade) {
			case 3:
				return DyeColor.colorFromBlockMeta(entityData.getByte(DATA_BLADE_COLOR) & MASK_BLADE_COLOR_FOUR);
			case 2:
				return DyeColor.colorFromBlockMeta(entityData.getByte(DATA_BLADE_COLOR) & MASK_BLADE_COLOR_THREE);
			case 1:
				return DyeColor.colorFromBlockMeta(entityData.getByte(DATA_BLADE_COLOR) & MASK_BLADE_COLOR_TWO);
			default:
				return DyeColor.colorFromBlockMeta(entityData.getByte(DATA_BLADE_COLOR) & MASK_BLADE_COLOR_ONE);
		}
	}
}
