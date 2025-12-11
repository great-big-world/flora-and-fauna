package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientLevel.class)
public abstract class ClientWorldMixin extends Level {
    protected ClientWorldMixin(WritableLevelData properties, ResourceKey<Level> registryRef, RegistryAccess registryManager, Holder<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Redirect(method = "calculateBlockTint", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ColorResolver;getColor(Lnet/minecraft/world/level/biome/Biome;DD)I"))
    private int gbw$modifyBlockColor(ColorResolver instance, Biome biome, double x, double z, @Local(argsOnly = true) BlockPos pos) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !getBlockState(pos).is(FloraAndFaunaTags.IGNORE_SEASON_COLOR) && dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            return FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(this, pos, instance.getColor(biome, x, z));
        }
        return instance.getColor(biome, x, z);
    }
}
