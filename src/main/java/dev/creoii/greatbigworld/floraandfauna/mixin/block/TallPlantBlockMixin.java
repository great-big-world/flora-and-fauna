package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.creoapi.api.block.CreoBlock;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TallPlantBlock.class)
public abstract class TallPlantBlockMixin extends PlantBlock implements CreoBlock {
    @Shadow @Final public static EnumProperty<DoubleBlockHalf> HALF;

    protected TallPlantBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$setSnowyDefaultState(Settings settings, CallbackInfo ci) {
        setDefaultState(stateManager.getDefaultState().with(HALF, DoubleBlockHalf.LOWER).with(SnowyHelper.SNOW_LAYERS, 0));
    }

    @Inject(method = "getPlacementState", at = @At("RETURN"), cancellable = true)
    private void gbw$applySnowyPlacementState(ItemPlacementContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (cir.getReturnValue().get(HALF) == DoubleBlockHalf.LOWER && state.isOf(Blocks.SNOW)) {
            cir.setReturnValue(cir.getReturnValue().with(SnowyHelper.SNOW_LAYERS, state.get(SnowBlock.LAYERS)));
        }
    }

    @Inject(method = "appendProperties", at = @At("TAIL"))
    private void gbw$addSnowyProperty(StateManager.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(SnowyHelper.SNOW_LAYERS);
    }

    @Inject(method = "onPlaced", at = @At("HEAD"), cancellable = true)
    private void gbw$fixSnowPlacementBug(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        if (SnowyHelper.isSnowy(state)) {
            ci.cancel();
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return SnowyHelper.isSnowy(state);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);
        if (world.getLightLevel(LightType.BLOCK, pos) > 11 || (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER && !biomeEntry.isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && biomeEntry.value().doesNotSnow(pos))) {
            if (state.get(SnowyHelper.SNOW_LAYERS) > 0)
                dropStacks(Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS)), world, pos);
            world.setBlockState(pos, state.with(SnowyHelper.SNOW_LAYERS, 0));
        }
    }

    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType type) {
        if (type == NavigationType.LAND)
            return state.get(SnowyHelper.SNOW_LAYERS) < 5;
        return false;
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.LAYERS_TO_SHAPE[state.get(SnowyHelper.SNOW_LAYERS) - 1];
        }
        return VoxelShapes.empty();
    }

    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return VoxelShapes.empty();
    }

    public VoxelShape getCameraCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (SnowyHelper.isSnowy(state)) {
            return SnowyHelper.getSnowShape(state);
        }
        return VoxelShapes.empty();
    }

    @Override
    public BlockState getOverlayState(BlockState state, BlockPos pos, Random random) {
        if (SnowyHelper.isSnowy(state)) {
            return Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, state.get(SnowyHelper.SNOW_LAYERS));
        }
        return Blocks.AIR.getDefaultState();
    }
}
