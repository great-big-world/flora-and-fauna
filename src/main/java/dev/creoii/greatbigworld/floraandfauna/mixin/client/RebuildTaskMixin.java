package dev.creoii.greatbigworld.floraandfauna.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.util.SnowyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.chunk.BlockBufferBuilderStorage;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkBuilder.BuiltChunk.RebuildTask.class)
public class RebuildTaskMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/BlockRenderManager;renderBlock(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/BlockRenderView;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;ZLnet/minecraft/util/math/random/Random;)V"))
    private void gbw$renderSnowOverlay(float cameraX, float cameraY, float cameraZ, BlockBufferBuilderStorage storage, CallbackInfoReturnable<ChunkBuilder.BuiltChunk.RebuildTask.RenderData> cir, @Local ChunkRendererRegion chunkRendererRegion, @Local MatrixStack matrixStack, @Local Random random, @Local BlockRenderManager blockRenderManager, @Local BlockState blockState, @Local(ordinal = 2) BlockPos blockPos3) {
        if (blockState.contains(SnowyHelper.SNOW_LAYERS) && blockState.get(SnowyHelper.SNOW_LAYERS) > 0) {
            BufferBuilder bufferBuilder = storage.get(RenderLayer.getSolid());
            if (bufferBuilder.isBuilding()) {
                blockRenderManager.renderBlock(Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, blockState.get(SnowyHelper.SNOW_LAYERS)), blockPos3, chunkRendererRegion, matrixStack, bufferBuilder, true, random);
            }
        }
    }
}
