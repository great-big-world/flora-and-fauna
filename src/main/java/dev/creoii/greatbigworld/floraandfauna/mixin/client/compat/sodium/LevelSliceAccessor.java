package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import net.caffeinemc.mods.sodium.client.world.LevelSlice;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Mixin(LevelSlice.class)
public interface LevelSliceAccessor {
    @Accessor("level")
    ClientLevel gbw$getLevel();
}