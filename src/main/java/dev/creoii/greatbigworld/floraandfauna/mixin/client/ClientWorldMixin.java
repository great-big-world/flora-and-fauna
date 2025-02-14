package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.ColorResolver;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    protected ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Redirect(method = "calculateColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/ColorResolver;getColor(Lnet/minecraft/world/biome/Biome;DD)I"))
    private int gbw$modifyBlockColor(ColorResolver instance, Biome biome, double x, double z, @Local(argsOnly = true) BlockPos pos) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !getBlockState(pos).isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            return FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(this, pos, instance.getColor(biome, x, z));
        }
        return 0;
    }
}
