package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpreadingSnowyDirtBlock.class)
public class SpreadingSnowyDirtBlockMixin {
    @Inject(method = "canBeGrass", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), cancellable = true)
    private static void gbw$fixIsSnow(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockState blockState2) {
        int maxSnowHeight = 1;
        if (!levelReader.isClientSide()) {
            maxSnowHeight = ((ServerLevel) levelReader).getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);
        }
        if (SnowyHelper.isSnowy(blockState2) && blockState2.getValue(SnowyHelper.SNOW_LAYERS) <= maxSnowHeight)
            cir.setReturnValue(true);
        else if (blockState2.is(Blocks.SNOW) && blockState2.getValue(SnowLayerBlock.LAYERS) <= maxSnowHeight)
            cir.setReturnValue(true);
    }
}
