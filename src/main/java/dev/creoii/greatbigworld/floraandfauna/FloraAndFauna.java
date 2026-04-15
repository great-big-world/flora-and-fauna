package dev.creoii.greatbigworld.floraandfauna;

import dev.creoii.greatbigworld.floraandfauna.registry.*;
import dev.creoii.greatbigworld.mixin.AbstractBlockStateAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Blocks;

public class FloraAndFauna implements ModInitializer {
    @Override
    public void onInitialize() {
        FloraAndFaunaBlocks.register();
        FloraAndFaunaItems.register();
        FloraAndFaunaFeatures.register();
        FloraAndFaunaCommands.register();
        FloraAndFaunaGameRules.register();
        FloraAndFaunaNetworking.register();
        FloraAndFaunaEvents.register();

        Blocks.BROWN_MUSHROOM.getStateDefinition().getPossibleStates().forEach(state -> ((AbstractBlockStateAccessor) state).setLightEmission(0));
        Blocks.SCULK_SHRIEKER.getStateDefinition().getPossibleStates().forEach(state -> ((AbstractBlockStateAccessor) state).setLightEmission(3));

        TillableBlockRegistry.register(FloraAndFaunaBlocks.HUMUS, HoeItem::onlyIfAirAbove, Blocks.FARMLAND.defaultBlockState());
    }
}
