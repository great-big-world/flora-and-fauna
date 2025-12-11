package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TerrainParticle.class)
public class BlockDustParticleMixin {
    @WrapOperation(method = "<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDDDDLnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/core/BlockPos;I)I"))
    private int gbw$tintBlockBreakParticle(BlockColors instance, BlockState state, BlockAndTintGetter blockRenderView, BlockPos pos, int tintIndex, Operation<Integer> original, @Local(argsOnly = true) ClientLevel world) {
        if (!world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && FloraAndFaunaClient.getCurrentSeason() != null) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                return FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, original.call(instance, state, world, pos, tintIndex));
            } else {
                return FloraAndFaunaClient.getCurrentSeason().getParticleColorChange().apply(new Season.Context(world, pos, original.call(instance, state, world, pos, tintIndex)));
            }
        }
        return original.call(instance, state, world, pos, tintIndex);
    }
}
