package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SmallDripleafBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmallDripleafBlock.class)
public abstract class SmallDripleafBlockMixin extends DoublePlantBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    @Shadow @Final private static BooleanProperty WATERLOGGED;
    @Shadow @Final public static EnumProperty<Direction> FACING;

    public SmallDripleafBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(BlockBehaviour.Properties settings, CallbackInfo ci) {
        registerDefaultState(stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, false).setValue(FACING, Direction.NORTH).setValue(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void gbw$addSnowyProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }
}
