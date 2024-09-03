package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockDustParticle.class)
public class BlockDustParticleMixin {
    @WrapOperation(method = "<init>(Lnet/minecraft/client/world/ClientWorld;DDDDDDLnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;getColor(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/util/math/BlockPos;I)I"))
    private int gbw$tintBlockBreakParticle(BlockColors instance, BlockState state, BlockRenderView blockRenderView, BlockPos pos, int tintIndex, Operation<Integer> original, @Local(argsOnly = true) ClientWorld world) {
        if (!state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            Season season = FloraAndFaunaClient.getCurrentSeason();
            if (season != null && !world.getBiome(pos).isIn(season.getBiomesNotAffectedBy())) {
                return season.getColorChangeParticle().apply(new Season.Context(world, pos, original.call(instance, state, world, pos, tintIndex)));
            }
        }
        return original.call(instance, state, world, pos, tintIndex);
    }
}
