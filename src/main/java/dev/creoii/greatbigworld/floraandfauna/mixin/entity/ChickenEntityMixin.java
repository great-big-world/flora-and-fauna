package dev.creoii.greatbigworld.floraandfauna.mixin.entity;

import dev.creoii.greatbigworld.floraandfauna.entity.DuckLike;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.ChickenEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChickenEntity.class)
public class ChickenEntityMixin implements DuckLike {
    @Unique
    @Nullable
    private LivingEntity gbw$follower;

    @Override
    public LivingEntity gbw$getFollowing() {
        return null;
    }

    @Override
    public LivingEntity gbw$getFollower() {
        return gbw$follower;
    }

    @Override
    public void gbw$setFollowing(LivingEntity duckLike) {}

    @Override
    public void gbw$setFollower(LivingEntity duckLike) {
        gbw$follower = duckLike;
    }
}
