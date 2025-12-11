package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    @Unique
    private static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

    public LeavesBlockMixin(Properties settings) {
        super(settings);
    }

    @ModifyExpressionValue(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;setValue(Lnet/minecraft/world/level/block/state/properties/Property;Ljava/lang/Comparable;)Ljava/lang/Object;", ordinal = 2))
    private Object gbw$initSnowyLeaves(Object original) {
        return ((BlockState) original).setValue(SNOWY, false);
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void gbw$addSnowyProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SNOWY);
    }

    @ModifyReturnValue(method = "updateShape", at = @At("RETURN"))
    private BlockState gbw$snowLeavesNeighborUpdate(BlockState original, @Local(argsOnly = true) Direction direction, @Local(argsOnly = true, ordinal = 1) BlockState neighborState) {
        return direction == Direction.UP ? original.setValue(SNOWY, isSnow(neighborState)) : original;
    }

    @ModifyReturnValue(method = "getStateForPlacement", at = @At("RETURN"))
    private BlockState gbw$snowLeavesPlacementState(BlockState original, @Local(argsOnly = true)BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().above());
        return original.setValue(SNOWY, isSnow(blockState));
    }

    @Unique
    private static boolean isSnow(BlockState state) {
        return state.is(BlockTags.SNOW);
    }
}
