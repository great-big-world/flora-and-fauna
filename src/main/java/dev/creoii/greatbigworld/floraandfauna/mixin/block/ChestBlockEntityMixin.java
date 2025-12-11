package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin {
    @Inject(method = "lidAnimateTick", at = @At("TAIL"))
    private static void gbw$underwaterChestBubbles(Level world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
        if (((!blockEntity.chestLidController.shouldBeOpen && blockEntity.chestLidController.openness > 0f) || (blockEntity.chestLidController.shouldBeOpen && blockEntity.chestLidController.openness < 1f)) && state.getValue(BlockStateProperties.WATERLOGGED) && world.getFluidState(pos.above()).is(FluidTags.WATER)) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            if (world.getRandom().nextInt(2) == 0) {
                world.addParticle(ParticleTypes.BUBBLE, x + .5f, y + .75f, z + .5f, 0f, .04f, 0f);
            }

            if (world.getRandom().nextInt(200) == 0) {
                world.playLocalSound(x, y, z, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, .2f + world.getRandom().nextFloat() * .2f, .9f + world.getRandom().nextFloat() * .15f, false);
            }
        }
    }
}
