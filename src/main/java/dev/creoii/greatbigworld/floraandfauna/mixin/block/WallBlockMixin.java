package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import dev.creoii.creoapi.api.block.CreoBlock;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(WallBlock.class)
public abstract class WallBlockMixin extends Block implements Waterloggable, CreoBlock {
    @Shadow @Final private Map<BlockState, VoxelShape> shapeMap;
    @Shadow @Final private Map<BlockState, VoxelShape> collisionShapeMap;

    public WallBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Settings settings, CallbackInfo ci) {
        setDefaultState(getStateManager().getDefaultState().with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Inject(method = "getOutlineShape", at = @At("HEAD"), cancellable = true)
    private void gbw$mergeSnowOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SnowyHelper.isSnowy(state)) {
            cir.setReturnValue(VoxelShapes.union(shapeMap.get(state.with(SnowyHelper.SNOW_LAYERS, 0)), SnowyHelper.getSnowShape(state)));
        }
    }

    @Inject(method = "getCollisionShape", at = @At("RETURN"), cancellable = true)
    private void gbw$mergeSnowCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (SnowyHelper.isSnowy(state)) {
            cir.setReturnValue(VoxelShapes.union(collisionShapeMap.get(state.with(SnowyHelper.SNOW_LAYERS, 0)), SnowyHelper.getSnowShape(state)));
        }
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void gbw$applyFenceGateSnowPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (state.isOf(Blocks.SNOW)) {
            cir.setReturnValue(cir.getReturnValue().with(SnowyHelper.SNOW_LAYERS, state.get(SnowBlock.LAYERS)));
        }
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void gbw$appendSnowLayersProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        SeasonManager seasonManager = SeasonManager.getInstance(world.getServer());
        if (world.getLightLevel(LightType.BLOCK, pos) > 11 || (seasonManager.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos))) {
            if (state.get(SnowyHelper.SNOW_LAYERS) > 0)
                dropStacks(Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS)), world, pos);
            world.setBlockState(pos, state.with(SnowyHelper.SNOW_LAYERS, 0));
        }
    }

    @Override
    public BlockState getOverlayState(BlockState state, BlockPos pos, Random random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.getDefaultState();
    }
}
