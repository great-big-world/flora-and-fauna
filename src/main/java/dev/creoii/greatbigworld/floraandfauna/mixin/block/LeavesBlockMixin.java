package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    @Unique
    private static final BooleanProperty SNOWY = Properties.SNOWY;

    public LeavesBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$initSnowyLeaves(float leafParticleChance, AbstractBlock.Settings settings, CallbackInfo ci) {
        setDefaultState(stateManager.getDefaultState().with(SNOWY, false));
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void gbw$addSnowyProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SNOWY);
    }

    @ModifyReturnValue(method = "getStateForNeighborUpdate", at = @At("RETURN"))
    private BlockState gbw$snowLeavesNeighborUpdate(BlockState original, @Local(argsOnly = true) Direction direction, @Local(argsOnly = true, ordinal = 1) BlockState neighborState) {
        return direction == Direction.UP ? original.with(SNOWY, isSnow(neighborState)) : original;
    }

    @ModifyReturnValue(method = "getPlacementState", at = @At("RETURN"))
    private BlockState gbw$snowLeavesPlacementState(BlockState original, @Local(argsOnly = true)ItemPlacementContext context) {
        BlockState blockState = context.getWorld().getBlockState(context.getBlockPos().up());
        return original.with(SNOWY, isSnow(blockState));
    }

    @Unique
    private static boolean isSnow(BlockState state) {
        return state.isIn(BlockTags.SNOW);
    }
}
