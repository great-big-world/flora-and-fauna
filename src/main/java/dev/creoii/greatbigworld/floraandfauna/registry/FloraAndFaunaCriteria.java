package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.advancement.DuckFollowChickenCriteria;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class FloraAndFaunaCriteria {
    public static final DuckFollowChickenCriteria DUCK_FOLLOW_CHICKEN = new DuckFollowChickenCriteria();

    public static void register() {
        Registry.register(Registries.CRITERION, new Identifier(FloraAndFauna.NAMESPACE, "duck_follow_parent"), DUCK_FOLLOW_CHICKEN);
    }
}
