package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {
    @Inject(method = "getParticleColor", at = @At(value = "RETURN", ordinal = 0), cancellable = true)
    private void gbw$modifyParticleColor(BlockState state, World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
            cir.setReturnValue(FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, cir.getReturnValue()));
        }
    }

    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;registerColorProvider(Lnet/minecraft/client/color/block/BlockColorProvider;[Lnet/minecraft/block/Block;)V", ordinal = 3))
    private static void gbw$modifySpruceColor(BlockColors instance, BlockColorProvider provider, Block[] blocks) {
        instance.registerColorProvider((state, world, pos, tintIndex) -> {
            if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
                return FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, -10380959);
            }
            return -10380959;
        }, Blocks.SPRUCE_LEAVES);
    }

    @Redirect(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;registerColorProvider(Lnet/minecraft/client/color/block/BlockColorProvider;[Lnet/minecraft/block/Block;)V", ordinal = 4))
    private static void gbw$modifyBirchColor(BlockColors instance, BlockColorProvider provider, Block[] blocks) {
        instance.registerColorProvider((state, world, pos, tintIndex) -> {
            if (FloraAndFaunaClient.getCurrentSeason() != null && FloraAndFaunaClient.getTransitionContext() != null && !state.isIn(FloraAndFaunaTags.IGNORE_SEASON_COLOR)) {
                return FloraAndFaunaClient.getTransitionContext().getSeasonGrassColor(world, pos, -8345771);
            }
            return -8345771;
        }, Blocks.BIRCH_LEAVES);
    }
}
