package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TintedParticleLeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TintedParticleLeavesBlock.class)
public class TintedParticleLeavesBlockMixin {
    @WrapOperation(method = "spawnFallingLeavesParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/particles/ColorParticleOption;create(Lnet/minecraft/core/particles/ParticleType;I)Lnet/minecraft/core/particles/ColorParticleOption;"))
    private ColorParticleOption gbw$tintLeavesParticlesForSeason(ParticleType<ColorParticleOption> type, int color, Operation<ColorParticleOption> original, @Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos pos) {
        if (world.isClientSide() && !world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && FloraAndFaunaClient.getCurrentSeason() != null) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                color = FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, color);
            } else {
                color = FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, color));
            }
        }
        return ColorParticleOption.create(type, color);
    }
}
