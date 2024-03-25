package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowyBlock.class)
public class SnowyBlockMixin {
    @Inject(method = "isSnow", at = @At("HEAD"), cancellable = true)
    private static void gbw$fixIsSnow(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (SnowyHelper.isSnowy(state))
            cir.setReturnValue(true);
    }
}
