package dev.creoii.greatbigworld.floraandfauna.util;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.client.model.DuckEntityModel;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public final class FloraAndFaunaEntityModelLayers {
    public static final EntityModelLayer DUCK = new EntityModelLayer(new Identifier(FloraAndFauna.NAMESPACE, "duck"), "main");

    public static void register() {
        EntityModelLayerRegistry.registerModelLayer(DUCK, DuckEntityModel::getTexturedModelData);
    }
}
