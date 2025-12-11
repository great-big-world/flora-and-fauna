package dev.creoii.greatbigworld.floraandfauna.mixin.world;

import com.mojang.datafixers.util.Pair;
import dev.creoii.greatbigworld.floraandfauna.registry.FloraAndFaunaBiomes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;

@Mixin(OverworldBiomeBuilder.class)
public abstract class VanillaBiomeParametersMixin {
    @Mutable @Shadow @Final private ResourceKey<Biome>[][] MIDDLE_BIOMES_VARIANT;
    @Shadow @Final private Climate.Parameter FULL_RANGE;
    @Shadow @Final private Climate.Parameter coastContinentalness;
    @Shadow @Final private Climate.Parameter[] erosions;
    @Shadow @Final private Climate.Parameter[] temperatures;
    @Shadow @Final private Climate.Parameter[] humidities;
    @Shadow @Final private Climate.Parameter farInlandContinentalness;
    @Shadow @Final private Climate.Parameter nearInlandContinentalness;
    @Shadow @Final private Climate.Parameter FROZEN_RANGE;
    @Shadow protected abstract void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome);

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("TAIL"))
    private void gbw$replaceOldGrowthBirchForest(CallbackInfo ci) {
        MIDDLE_BIOMES_VARIANT = new ResourceKey[][]{{Biomes.ICE_SPIKES, null, Biomes.SNOWY_TAIGA, null, null}, {null, null, null, null, Biomes.OLD_GROWTH_PINE_TAIGA}, {Biomes.SUNFLOWER_PLAINS, null, null, Biomes.BIRCH_FOREST, null}, {null, null, Biomes.PLAINS, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE}, {null, null, null, null, null}};
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 1))
    private void gbw$writeRiverBiomes1(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, temperatures[4], FULL_RANGE, coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : FloraAndFaunaBiomes.DESERT_RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[3], humidities[4]), coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : FloraAndFaunaBiomes.JUNGLE_RIVER);
        addSurfaceBiome(parameters, temperatures[1], Climate.Parameter.span(humidities[3], humidities[4]), coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : FloraAndFaunaBiomes.COLD_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[2], temperatures[4]), FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : FloraAndFaunaBiomes.SWAMP_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[1], temperatures[2]), Climate.Parameter.span(humidities[0], humidities[2]), coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[0], humidities[2]), coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 3))
    private void gbw$writeRiverBiomes3(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, temperatures[4], FULL_RANGE, nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, FloraAndFaunaBiomes.DESERT_RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[3], humidities[4]), nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, FloraAndFaunaBiomes.JUNGLE_RIVER);
        addSurfaceBiome(parameters, temperatures[1], Climate.Parameter.span(humidities[3], humidities[4]), nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[2], temperatures[4]), FULL_RANGE, nearInlandContinentalness, erosions[6], weirdness, 0f, FloraAndFaunaBiomes.SWAMP_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[1], temperatures[2]), Climate.Parameter.span(humidities[0], humidities[2]), nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, Biomes.RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[0], humidities[2]), nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, Biomes.RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 5))
    private void gbw$writeRiverBiomes5(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, temperatures[4], FULL_RANGE, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, FloraAndFaunaBiomes.DESERT_RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[3], humidities[4]), Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, FloraAndFaunaBiomes.JUNGLE_RIVER);
        addSurfaceBiome(parameters, temperatures[1], Climate.Parameter.span(humidities[3], humidities[4]), Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[2], temperatures[4]), FULL_RANGE, Climate.Parameter.span(coastContinentalness, farInlandContinentalness), erosions[6], weirdness, 0f, FloraAndFaunaBiomes.SWAMP_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[1], temperatures[2]), Climate.Parameter.span(humidities[0], humidities[2]), Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, Biomes.RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[0], humidities[2]), Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, Biomes.RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 7))
    private void gbw$writeRiverBiomes7(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, temperatures[4], FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0f, FloraAndFaunaBiomes.DESERT_RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[3], humidities[4]), coastContinentalness, erosions[6], weirdness, 0f, FloraAndFaunaBiomes.JUNGLE_RIVER);
        addSurfaceBiome(parameters, temperatures[1], Climate.Parameter.span(humidities[3], humidities[4]), coastContinentalness, erosions[6], weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[2], temperatures[4]), FULL_RANGE, coastContinentalness, erosions[6], weirdness, 0f, FloraAndFaunaBiomes.SWAMP_RIVER);
        addSurfaceBiome(parameters, Climate.Parameter.span(temperatures[1], temperatures[2]), Climate.Parameter.span(humidities[0], humidities[2]), coastContinentalness, erosions[6], weirdness, 0f, Biomes.RIVER);
        addSurfaceBiome(parameters, temperatures[3], Climate.Parameter.span(humidities[0], humidities[2]), coastContinentalness, erosions[6], weirdness, 0f, Biomes.RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 0))
    private void gbw$writeRiverBiomes0(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, FROZEN_RANGE, Climate.Parameter.span(temperatures[0], temperatures[3]), coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : Biomes.FROZEN_RIVER);
        addSurfaceBiome(parameters, FROZEN_RANGE, humidities[4], coastContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, weirdness.max() < 0L ? Biomes.STONY_SHORE : FloraAndFaunaBiomes.COLD_RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 2))
    private void gbw$writeRiverBiomes2(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, FROZEN_RANGE, Climate.Parameter.span(temperatures[0], temperatures[3]), nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, Biomes.FROZEN_RIVER);
        addSurfaceBiome(parameters, FROZEN_RANGE, humidities[4], nearInlandContinentalness, Climate.Parameter.span(erosions[0], erosions[1]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 4))
    private void gbw$writeRiverBiomes4(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, FROZEN_RANGE, Climate.Parameter.span(temperatures[0], temperatures[3]), Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, Biomes.FROZEN_RIVER);
        addSurfaceBiome(parameters, FROZEN_RANGE, humidities[4], Climate.Parameter.span(coastContinentalness, farInlandContinentalness), Climate.Parameter.span(erosions[2], erosions[5]), weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
    }

    @Redirect(method = "addValleys", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addSurfaceBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V", ordinal = 6))
    private void gbw$writeRiverBiomes6(OverworldBiomeBuilder instance, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        addSurfaceBiome(parameters, FROZEN_RANGE, Climate.Parameter.span(temperatures[0], temperatures[3]), coastContinentalness, erosions[6], weirdness, 0f, Biomes.FROZEN_RIVER);
        addSurfaceBiome(parameters, FROZEN_RANGE, humidities[4], coastContinentalness, erosions[6], weirdness, 0f, FloraAndFaunaBiomes.COLD_RIVER);
    }
}
