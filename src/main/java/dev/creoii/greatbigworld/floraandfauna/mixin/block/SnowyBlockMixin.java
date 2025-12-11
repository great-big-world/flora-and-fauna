package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowyDirtBlock.class)
public class SnowyBlockMixin {
    @Inject(method = "isSnowySetting", at = @At("HEAD"), cancellable = true)
    private static void gbw$fixIsSnow(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (SnowyHelper.isSnowy(state))
            cir.setReturnValue(true);
    }
}
