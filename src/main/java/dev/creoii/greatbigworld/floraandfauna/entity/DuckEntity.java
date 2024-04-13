package dev.creoii.greatbigworld.floraandfauna.entity;

import dev.creoii.greatbigworld.floraandfauna.entity.goal.DuckCaravanGoal;
import dev.creoii.greatbigworld.floraandfauna.entity.goal.WanderAroundFarInWaterGoal;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaEntities;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class DuckEntity extends AnimalEntity implements DuckLike {
    private static final TrackedData<Optional<UUID>> FOLLOWING = DataTracker.registerData(DuckEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    private static final Ingredient BREEDING_INGREDIENT = Ingredient.ofItems(Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS, Items.TORCHFLOWER_SEEDS, Items.PITCHER_POD);
    public float flapProgress;
    public float maxWingDeviation;
    public float prevMaxWingDeviation;
    public float prevFlapProgress;
    public float flapSpeed = 1f;
    @Nullable private LivingEntity following;
    @Nullable private LivingEntity follower;

    public DuckEntity(EntityType<? extends DuckEntity> entityType, World world) {
        super(entityType, world);
        setPathfindingPenalty(PathNodeType.WATER, 0f);
        setPathfindingPenalty(PathNodeType.WATER_BORDER, 0f);
    }

    protected void initGoals() {
        goalSelector.add(0, new SwimGoal(this));
        goalSelector.add(1, new EscapeDangerGoal(this, 1.2d));
        goalSelector.add(2, new AnimalMateGoal(this, 1d));
        goalSelector.add(3, new TemptGoal(this, 1d, BREEDING_INGREDIENT, false));
        goalSelector.add(4, new DuckCaravanGoal(this, 5, .85d));
        goalSelector.add(5, new WanderAroundFarInWaterGoal(this, .75d, .01f));
        goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 5f));
        goalSelector.add(7, new LookAroundGoal(this));
    }

    public static DefaultAttributeContainer.Builder createDuckAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 4d).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, .25d);
    }

    @Override
    protected EntityNavigation createNavigation(World world) {
        return new AmphibiousSwimNavigation(this, world);
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        if (world.isWater(getBlockPos()) || world.isWater(getBlockPos().down())) {
            BlockPos.Mutable mutable = getBlockPos().mutableCopy();
            for (int y = getBlockY(), depth = 0; y > world.getBottomY(); --y) {
                mutable.setY(y);
                if (world.isWater(mutable))
                    ++depth;
                else return true;

                if (depth > 4)
                    return false;
            }
        }
        return super.canSpawn(world, spawnReason);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        DuckEntity child = FloraAndFaunaEntities.DUCK.create(world);
        if (child != null) {
            child.follow(this);
        }
        return child;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        dataTracker.startTracking(FOLLOWING, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (gbw$getFollowing() != null)
            nbt.putUuid("Following", gbw$getFollowing().getUuid());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Following")) {
            UUID uuid = nbt.getUuid("Following");
            if (!getWorld().isClient && ((ServerWorld) getWorld()).getEntity(uuid) instanceof MobEntity mob) {
                follow(mob);
            }
        }
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        BlockState down = world.getBlockState(pos.down());
        if (down.isOf(Blocks.GRASS_BLOCK) || world.isWater(pos)) {
            return 10f;
        }
        return world.getPhototaxisFavor(pos);
    }

    public void tickMovement() {
        super.tickMovement();
        prevFlapProgress = flapProgress;
        prevMaxWingDeviation = maxWingDeviation;
        maxWingDeviation += (isOnGround() ? -1f : 4f) * .3f;
        maxWingDeviation = MathHelper.clamp(maxWingDeviation, 0f, 1f);
        if (!isOnGround() && flapSpeed < 1f) {
            flapSpeed = 1f;
        }

        flapSpeed *= .9f;
        Vec3d vec3d = getVelocity();
        if (!isOnGround() && vec3d.y < 0d) {
            setVelocity(vec3d.multiply(1d, .75d, 1d));
        }

        flapProgress += flapSpeed * 2f;
    }

    public void travel(Vec3d movementInput) {
        if (isLogicalSideForUpdatingMovement() && isTouchingWater()) {
            updateVelocity(getMovementSpeed(), movementInput);
            move(MovementType.SELF, getVelocity());
            Vec3d velocity = getVelocity();
            if (horizontalCollision) {
                setVelocity(new Vec3d(velocity.x, .30000001192092896d, velocity.z).multiply(getBaseMovementSpeedMultiplier(), .800000011920929d, getBaseMovementSpeedMultiplier()));
            } else
                setVelocity(velocity.multiply(.5d));
        } else super.travel(movementInput);
    }

    protected boolean isFlappingWings() {
        return speed > 1f;
    }

    protected SoundEvent getAmbientSound() {
        return FloraAndFaunaSoundEvents.ENTITY_DUCK_QUACK;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return FloraAndFaunaSoundEvents.ENTITY_DUCK_HURT;
    }

    protected SoundEvent getDeathSound() {
        return FloraAndFaunaSoundEvents.ENTITY_DUCK_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
        playSound(SoundEvents.ENTITY_CHICKEN_STEP, .15f, 1f);
    }

    @Override
    public void playAmbientSound() {
        if (random.nextInt(4) == 0)
            super.playAmbientSound();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return BREEDING_INGREDIENT.test(stack);
    }

    @Override
    public void gbw$setFollowing(@Nullable LivingEntity living) {
        following = living;
        dataTracker.set(FOLLOWING, living == null ? Optional.empty() : Optional.of(living.getUuid()));
    }

    @Override
    public void gbw$setFollower(@Nullable LivingEntity living) {
        if (living instanceof DuckLike duckLike) {
            duckLike.gbw$setFollowing(this);
            follower = living;
        }
    }

    @Override
    @Nullable
    public LivingEntity gbw$getFollowing() {
        return following;
    }

    @Override
    @Nullable
    public LivingEntity gbw$getFollower() {
        return follower;
    }

    @Override
    protected void onGrowUp() {
        if (gbw$getFollowing() != null) {
            stopFollowing();
        }
        super.onGrowUp();
    }

    public double getSwimHeight() {
        return .3d;
    }

    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        if (entityData == null) {
            entityData = new DuckData();
        }
        DuckData duckData = (DuckData) entityData;
        if (++duckData.spawnCount > 1) {
            setBreedingAge(-24000);
            LivingEntity currentDuck = duckData.parent;
            int length = 0;
            while (currentDuck instanceof DuckLike duckLike && duckLike.hasFollower() && length <= 4) {
                LivingEntity nextDuck = duckLike.gbw$getFollower();
                if (nextDuck instanceof DuckLike duckLike1 && duckLike1.hasFollower()) {
                    currentDuck = nextDuck;
                    ++length;
                } else {
                    if (nextDuck instanceof DuckLike duckLike1) {
                        if ((!duckLike1.isFollowing() && !nextDuck.isBaby()) || (nextDuck.isBaby() && !duckLike1.hasFollower())) {
                            follow(nextDuck);
                            continue;
                        }
                    }
                    break;
                }
            }
        } else
            duckData.parent = this;
        return duckData;
    }

    public static class DuckData implements EntityData {
        private DuckEntity parent;
        private int spawnCount;
    }
}
