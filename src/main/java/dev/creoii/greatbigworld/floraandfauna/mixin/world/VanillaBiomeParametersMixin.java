package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.mojang.datafixers.util.Pair;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBiomes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public abstract class VanillaBiomeParametersMixin {
    @Mutable @Shadow @Final private RegistryKey<Biome>[][] uncommonBiomes;
    @Shadow @Final private MultiNoiseUtil.ParameterRange defaultParameter;
    @Shadow @Final private MultiNoiseUtil.ParameterRange coastContinentalness;
    @Shadow @Final private MultiNoiseUtil.ParameterRange[] erosionParameters;
    @Shadow @Final private MultiNoiseUtil.ParameterRange[] temperatureParameters;
    @Shadow @Final private MultiNoiseUtil.ParameterRange[] humidityParameters;
    @Shadow @Final private MultiNoiseUtil.ParameterRange farInlandContinentalness;
    @Shadow @Final private MultiNoiseUtil.ParameterRange nearInlandContinentalness;
    @Shadow @Final private MultiNoiseUtil.ParameterRange frozenTemperature;
    @Shadow protected abstract void writeBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome);

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$replaceOldGrowthBirchForest(CallbackInfo ci) {
        uncommonBiomes = new RegistryKey[][]{{BiomeKeys.ICE_SPIKES, null, BiomeKeys.SNOWY_TAIGA, null, null}, {null, null, null, null, BiomeKeys.OLD_GROWTH_PINE_TAIGA}, {BiomeKeys.SUNFLOWER_PLAINS, null, null, BiomeKeys.BIRCH_FOREST, null}, {null, null, BiomeKeys.PLAINS, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE}, {null, null, null, null, null}};
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 1))
    private void gbw$writeRiverBiomes1(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, temperatureParameters[4], defaultParameter, coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : FloraAndFaunaBiomes.DESERT_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : FloraAndFaunaBiomes.JUNGLE_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[1], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : FloraAndFaunaBiomes.COLD_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[2], temperatureParameters[4]), defaultParameter, coastContinentalness, erosionParameters[6], weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : FloraAndFaunaBiomes.SWAMP_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[1], temperatureParameters[2]), MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : BiomeKeys.RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : BiomeKeys.RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 3))
    private void gbw$writeRiverBiomes3(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, temperatureParameters[4], defaultParameter, nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, FloraAndFaunaBiomes.DESERT_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, FloraAndFaunaBiomes.JUNGLE_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[1], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[2], temperatureParameters[4]), defaultParameter, nearInlandContinentalness, erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.SWAMP_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[1], temperatureParameters[2]), MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, BiomeKeys.RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, BiomeKeys.RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 5))
    private void gbw$writeRiverBiomes5(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, temperatureParameters[4], defaultParameter, MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, FloraAndFaunaBiomes.DESERT_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, FloraAndFaunaBiomes.JUNGLE_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[1], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[2], temperatureParameters[4]), defaultParameter, MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.SWAMP_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[1], temperatureParameters[2]), MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, BiomeKeys.RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, BiomeKeys.RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 7))
    private void gbw$writeRiverBiomes7(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, temperatureParameters[4], defaultParameter, coastContinentalness, erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.DESERT_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), coastContinentalness, erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.JUNGLE_RIVER);
        writeBiomeParameters(parameters, temperatureParameters[1], MultiNoiseUtil.ParameterRange.combine(humidityParameters[3], humidityParameters[4]), coastContinentalness, erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[2], temperatureParameters[4]), defaultParameter, coastContinentalness, erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.SWAMP_RIVER);
        writeBiomeParameters(parameters, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[1], temperatureParameters[2]), MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), coastContinentalness, erosionParameters[6], weirdness, 0f, BiomeKeys.RIVER);
        writeBiomeParameters(parameters, temperatureParameters[3], MultiNoiseUtil.ParameterRange.combine(humidityParameters[0], humidityParameters[2]), coastContinentalness, erosionParameters[6], weirdness, 0f, BiomeKeys.RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 0))
    private void gbw$writeRiverBiomes0(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, frozenTemperature, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[0], temperatureParameters[3]), coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : BiomeKeys.FROZEN_RIVER);
        writeBiomeParameters(parameters, frozenTemperature, humidityParameters[4], coastContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, weirdness.max() < 0L ? BiomeKeys.STONY_SHORE : FloraAndFaunaBiomes.COLD_RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 2))
    private void gbw$writeRiverBiomes2(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, frozenTemperature, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[0], temperatureParameters[3]), nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, BiomeKeys.FROZEN_RIVER);
        writeBiomeParameters(parameters, frozenTemperature, humidityParameters[4], nearInlandContinentalness, MultiNoiseUtil.ParameterRange.combine(erosionParameters[0], erosionParameters[1]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 4))
    private void gbw$writeRiverBiomes4(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, frozenTemperature, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[0], temperatureParameters[3]), MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, BiomeKeys.FROZEN_RIVER);
        writeBiomeParameters(parameters, frozenTemperature, humidityParameters[4], MultiNoiseUtil.ParameterRange.combine(coastContinentalness, farInlandContinentalness), MultiNoiseUtil.ParameterRange.combine(erosionParameters[2], erosionParameters[5]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
    }

    @Redirect(method = "writeValleyBiomes", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeBiomeParameters(Ljava/util/function/Consumer;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$ParameterRange;FLnet/minecraft/registry/RegistryKey;)V", ordinal = 6))
    private void gbw$writeRiverBiomes6(VanillaBiomeParameters instance, Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange weirdness, float offset, RegistryKey<Biome> biome) {
        writeBiomeParameters(parameters, frozenTemperature, MultiNoiseUtil.ParameterRange.combine(temperatureParameters[0], temperatureParameters[3]), coastContinentalness, erosionParameters[6], weirdness, 0f, BiomeKeys.FROZEN_RIVER);
        writeBiomeParameters(parameters, frozenTemperature, humidityParameters[4], coastContinentalness, erosionParameters[6], weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
    }
}
