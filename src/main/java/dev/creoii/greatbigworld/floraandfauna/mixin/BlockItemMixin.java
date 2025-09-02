package dev.creoii.greatbigworld.floraandfauna.mixin;

import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "getPlaceSound", at = @At("HEAD"), cancellable = true)
    private void gbw$fixSnowloggingPlacementSound(BlockState state, CallbackInfoReturnable<SoundEvent> cir) {
        if (SnowyHelper.isSnowy(state))
            cir.setReturnValue(SoundEvents.BLOCK_SNOW_BREAK);
    }
}
