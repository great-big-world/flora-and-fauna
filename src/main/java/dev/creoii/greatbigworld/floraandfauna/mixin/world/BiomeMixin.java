package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin {
    @WrapOperation(method = "canSetSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;doesNotSnow(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean gbw$modifyCanSetSnowForWinter(Biome instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) WorldView world) {
        return original.call(instance, pos) && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos) && pos.getY() >= world.getBottomY() && pos.getY() < world.getTopY() && world.getLightLevel(LightType.BLOCK, pos) < 10);
    }

    @WrapOperation(method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;doesNotSnow(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean gbw$modifyCanSetIceForWinter(Biome instance, BlockPos pos, Operation<Boolean> original, @Local(argsOnly = true) WorldView world) {
        return original.call(instance, pos) && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
    }
}
