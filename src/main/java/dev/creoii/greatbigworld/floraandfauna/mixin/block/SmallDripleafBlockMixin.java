package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmallDripleafBlock.class)
public abstract class SmallDripleafBlockMixin extends TallPlantBlock implements Fertilizable, Waterloggable {
    @Shadow @Final private static BooleanProperty WATERLOGGED;
    @Shadow @Final public static DirectionProperty FACING;

    public SmallDripleafBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(AbstractBlock.Settings settings, CallbackInfo ci) {
        setDefaultState(stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(WATERLOGGED, false).with(FACING, Direction.NORTH).with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void gbw$addSnowyProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }
}
