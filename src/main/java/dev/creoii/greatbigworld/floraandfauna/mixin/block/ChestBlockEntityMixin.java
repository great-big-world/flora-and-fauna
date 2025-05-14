package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public class ChestBlockEntityMixin {
    @Inject(method = "clientTick", at = @At("TAIL"))
    private static void gbw$underwaterChestBubbles(World world, BlockPos pos, BlockState state, ChestBlockEntity blockEntity, CallbackInfo ci) {
        if (((!blockEntity.lidAnimator.open && blockEntity.lidAnimator.progress > 0f) || (blockEntity.lidAnimator.open && blockEntity.lidAnimator.progress < 1f)) && state.get(Properties.WATERLOGGED) && world.getFluidState(pos.up()).isIn(FluidTags.WATER)) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            if (world.getRandom().nextInt(2) == 0) {
                world.addParticleClient(ParticleTypes.BUBBLE, x + .5f, y + .75f, z + .5f, 0f, .04f, 0f);
            }

            if (world.getRandom().nextInt(200) == 0) {
                world.playSoundClient(x, y, z, SoundEvents.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundCategory.BLOCKS, .2f + world.getRandom().nextFloat() * .2f, .9f + world.getRandom().nextFloat() * .15f, false);
            }
        }
    }
}
