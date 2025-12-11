package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getColor(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, Level world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && !world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS) && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR) && world.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS)) {
            if (FloraAndFaunaClient.getTransitionContext() != null) {
                cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
            } else {
                cir.setReturnValue(FloraAndFaunaClient.getCurrentSeason().getParticleColorChange().apply(new Season.Context(world, pos, cir.getReturnValue())));
            }
        }
    }

    @Redirect(method = "createDefault", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;register(Lnet/minecraft/client/color/block/BlockColor;[Lnet/minecraft/world/level/block/Block;)V", ordinal = 3))
    private static void gbw$modifySpruceColor(BlockColors instance, BlockColor provider, Block[] blocks) {
        instance.register((state, world, pos, tintIndex) -> {
            if (world instanceof ClientLevel clientWorld && !clientWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                return -8345771;
            if (FloraAndFaunaClient.getCurrentSeason() != null && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    return FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, -10380959);
                } else {
                    return FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, -10380959));
                }
            }
            return -10380959;
        }, Blocks.SPRUCE_LEAVES);
    }

    @Redirect(method = "createDefault", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;register(Lnet/minecraft/client/color/block/BlockColor;[Lnet/minecraft/world/level/block/Block;)V", ordinal = 4))
    private static void gbw$modifyBirchColor(BlockColors instance, BlockColor provider, Block[] blocks) {
        instance.register((state, world, pos, tintIndex) -> {
            if (world instanceof ClientLevel clientWorld && !clientWorld.dimensionTypeRegistration().is(FloraAndFaunaTags.AFFECTED_BY_SEASONS))
                return -8345771;
            if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.is(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
                if (FloraAndFaunaClient.getTransitionContext() != null) {
                    return FloraAndFaunaClient.getTransitionContext().getSeasonFoliageColor(world, pos, -8345771);
                } else {
                    return FloraAndFaunaClient.getCurrentSeason().getFoliageColorChange().apply(new Season.Context(world, pos, -8345771));
                }
            }
            return -8345771;
        }, Blocks.BIRCH_LEAVES);
    }
}
