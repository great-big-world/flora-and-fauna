package dev.creoii.greatbigworld.floraandfauna.mixin.client.compat.sodium;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import me.jellysquid.mods.sodium.client.model.color.DefaultColorProviders;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({
        DefaultColorProviders.FoliageColorProvider.class,
        DefaultColorProviders.GrassColorProvider.class
})
public class DefaultColorProvidersMixin {
    @Inject(method = "getColor", at = @At("RETURN"), cancellable = true, remap = false)
    private void gbw$modifyBlockColor(WorldSlice world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !world.getBlockState(x, y, z).isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR) && FloraAndFaunaClient.getTransitionContext() != null) {
            cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonColor(world, new BlockPos(x, y, z), cir.getReturnValue()));
        }
    }
}
