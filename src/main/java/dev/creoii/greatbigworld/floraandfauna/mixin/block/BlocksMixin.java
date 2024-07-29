package dev.creoii.greatbigworld.floraandfauna.mixin.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Blocks.class)
public class BlocksMixin {
    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ShortPlantBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyShortPlantSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SeagrassBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifySeagrassSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TallSeagrassBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyTallSeagrassSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FlowerBlock;<init>(Lnet/minecraft/registry/entry/RegistryEntry;FLnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyFlowerSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/WitherRoseBlock;<init>(Lnet/minecraft/registry/entry/RegistryEntry;FLnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyWitherRoseSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TallFlowerBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyTallFlowerSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/TallPlantBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyTallPlantSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/SmallDripleafBlock;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifySmallDripleafSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }

    @ModifyArg(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/MushroomPlantBlock;<init>(Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/block/AbstractBlock$Settings;)V"))
    private static AbstractBlock.Settings gbw$modifyMushroomPlantSettings(AbstractBlock.Settings settings) {
        return settings.dynamicBounds();
    }
}
