package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CactusBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {
    @ModifyExpressionValue(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CactusBlock;canSurvive(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean gbw$dontGrowCactusFlowersInOverworld(boolean original, @Local(argsOnly = true) ServerLevel world) {
        return world.dimension() != Level.OVERWORLD;
    }
}
