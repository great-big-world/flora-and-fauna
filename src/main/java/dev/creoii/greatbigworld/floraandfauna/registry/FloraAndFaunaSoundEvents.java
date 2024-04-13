package dev.creoii.greatbigworld.floraandfauna.registry;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public final class FloraAndFaunaSoundEvents {
    public static final SoundEvent ENTITY_DUCK_QUACK = SoundEvent.of(new Identifier(FloraAndFauna.NAMESPACE, "entity.duck.quack"));
    public static final SoundEvent ENTITY_DUCK_HURT = SoundEvent.of(new Identifier(FloraAndFauna.NAMESPACE, "entity.duck.hurt"));
    public static final SoundEvent ENTITY_DUCK_DEATH = SoundEvent.of(new Identifier(FloraAndFauna.NAMESPACE, "entity.duck.death"));

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, new Identifier(FloraAndFauna.NAMESPACE, "entity.duck.quack"), ENTITY_DUCK_QUACK);
        Registry.register(Registries.SOUND_EVENT, new Identifier(FloraAndFauna.NAMESPACE, "entity.duck.hurt"), ENTITY_DUCK_HURT);
        Registry.register(Registries.SOUND_EVENT, new Identifier(FloraAndFauna.NAMESPACE, "entity.duck.death"), ENTITY_DUCK_DEATH);
    }
}
