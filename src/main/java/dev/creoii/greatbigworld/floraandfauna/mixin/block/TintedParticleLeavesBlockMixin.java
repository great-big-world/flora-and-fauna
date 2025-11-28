package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.TintedParticleLeavesBlock;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TintedParticleLeavesBlock.class)
public class TintedParticleLeavesBlockMixin {
    @WrapOperation(method = "spawnLeafParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/particle/TintedParticleEffect;create(Lnet/minecraft/particle/ParticleType;I)Lnet/minecraft/particle/TintedParticleEffect;"))
    private TintedParticleEffect gbw$tintLeavesParticlesForSeason(ParticleType<TintedParticleEffect> type, int color, Operation<TintedParticleEffect> original, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
        if (world.isClient() && !world.getDimensionEntry().isIn(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && FloraAndFaunaClient.getCurrentSeason() != null) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                color = FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, color);
            } else {
                color = FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, color));
            }
        }
        return TintedParticleEffect.create(type, color);
    }
}
