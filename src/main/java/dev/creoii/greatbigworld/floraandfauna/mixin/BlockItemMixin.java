package dev.creoii.greatbigworld.floraandfauna.mixin;

import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "getPlaceSound", at = @At("HEAD"), cancellable = true)
    private void gbw$fixSnowloggingPlacementSound(BlockState state, CallbackInfoReturnable<SoundEvent> cir) {
        if (SnowyHelper.isSnowy(state))
            cir.setReturnValue(SoundEvents.SNOW_BREAK);
    }
}
