package dev.creoii.greatbigworld.floraandfauna.entity;

import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface DuckLike {
    @Nullable
    LivingEntity gbw$getFollowing();

    @Nullable
    LivingEntity gbw$getFollower();

    void gbw$setFollowing(LivingEntity duckLike);

    void gbw$setFollower(LivingEntity duckLike);

    default boolean hasFollower() {
        return gbw$getFollower() != null;
    }

    default boolean isFollowing() {
        return gbw$getFollowing() != null;
    }

    default void stopFollowing() {
        if (gbw$getFollowing() instanceof DuckLike duckLike) {
            duckLike.gbw$setFollower(null);
        }
        gbw$setFollowing(null);
    }

    default void follow(LivingEntity living) {
        gbw$setFollowing(living);
        if (gbw$getFollowing() instanceof DuckLike duckLike)
            duckLike.gbw$setFollower(null);
    }
}
