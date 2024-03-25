package dev.creoii.greatbigworld.floraandfauna.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;
import java.util.function.Predicate;

public class DuckFollowChickenCriteria extends AbstractCriterion<DuckFollowChickenCriteria.Conditions> {
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    @Override
    public void trigger(ServerPlayerEntity player, Predicate<Conditions> predicate) {
        super.trigger(player, predicate);
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
            return instance.group(Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player").forGetter(conditions -> {
                return conditions.player;
            })).apply(instance, Conditions::new);
        });
    }
}
