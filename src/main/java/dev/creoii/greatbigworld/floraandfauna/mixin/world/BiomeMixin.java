package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.creoii.greatbigworld.GreatBigWorld;
import dev.creoii.greatbigworld.floraandfauna.client.FloraAndFaunaClient;
import dev.creoii.greatbigworld.floraandfauna.season.Season;
import dev.creoii.greatbigworld.floraandfauna.season.SeasonManager;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaTags;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Biome.class)
public class BiomeMixin {
    @WrapOperation(method = "canSetSnow", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;getPrecipitation(Lnet/minecraft/util/math/BlockPos;I)Lnet/minecraft/world/biome/Biome$Precipitation;"))
    private Biome.Precipitation gbw$modifyCanSetSnowForWinter(Biome instance, BlockPos pos, int seaLevel, Operation<Biome.Precipitation> original, @Local(argsOnly = true) WorldView world) {
        if (world.isClient() && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos) && world.isInHeightLimit(pos.getY()) && world.getLightLevel(LightType.BLOCK, pos) < 10))
            return Biome.Precipitation.SNOW;
        else {
            if (world instanceof ServerWorld serverWorld) {
                SeasonManager seasonManager = SeasonManager.getInstance(serverWorld.getServer());
                if (seasonManager == null || !serverWorld.getRegistryKey().equals(GreatBigWorld.ALTERWORLD_KEY))
                    return original.call(instance, pos, seaLevel);
                return (seasonManager.getCurrentSeason() != Season.WINTER || world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER) && Blocks.SNOW.getDefaultState().canPlaceAt(world, pos) && world.isInHeightLimit(pos.getY()) && world.getLightLevel(LightType.BLOCK, pos) < 10) ? Biome.Precipitation.SNOW : original.call(instance, pos, seaLevel);
            } else return original.call(instance, pos, seaLevel);
        }
    }

    @WrapOperation(method = "canSetIce(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/Biome;doesNotSnow(Lnet/minecraft/util/math/BlockPos;I)Z"))
    private boolean gbw$modifyCanSetIceForWinter(Biome instance, BlockPos pos, int seaLevel, Operation<Boolean> original, @Local(argsOnly = true) WorldView world) {
        if (world.isClient())
            return original.call(instance, pos, seaLevel) && (FloraAndFaunaClient.getCurrentSeason() != Season.WINTER || world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
        else {
            if (world instanceof ServerWorld serverWorld) {
                SeasonManager seasonManager = SeasonManager.getInstance(serverWorld.getServer());
                if (seasonManager == null || !serverWorld.getRegistryKey().equals(GreatBigWorld.ALTERWORLD_KEY))
                    return original.call(instance, pos, seaLevel);
                return original.call(instance, pos, seaLevel) && (seasonManager.getCurrentSeason() != Season.WINTER || world.getBiome(pos).isIn(FloraAndFaunaTags.NOT_AFFECTED_BY_WINTER));
            } else return original.call(instance, pos, seaLevel);
        }
    }
}
