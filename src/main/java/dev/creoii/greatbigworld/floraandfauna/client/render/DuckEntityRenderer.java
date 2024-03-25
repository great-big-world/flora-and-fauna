package dev.creoii.greatbigworld.floraandfauna.client.render;

import dev.creoii.greatbigworld.floraandfauna.FloraAndFauna;
import dev.creoii.greatbigworld.floraandfauna.client.model.DuckEntityModel;
import dev.creoii.greatbigworld.floraandfauna.entity.DuckEntity;
import dev.creoii.greatbigworld.floraandfauna.util.FloraAndFaunaEntityModelLayers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class DuckEntityRenderer extends MobEntityRenderer<DuckEntity, DuckEntityModel> {
    private static final Identifier TEXTURE = new Identifier(FloraAndFauna.NAMESPACE, "textures/entity/duck.png");

    public DuckEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new DuckEntityModel(context.getPart(FloraAndFaunaEntityModelLayers.DUCK)), .3f);
    }

    public Identifier getTexture(DuckEntity duckEntity) {
        return TEXTURE;
    }

    protected float getAnimationProgress(DuckEntity duckEntity, float f) {
        float g = MathHelper.lerp(f, duckEntity.prevFlapProgress, duckEntity.flapProgress);
        float h = MathHelper.lerp(f, duckEntity.prevMaxWingDeviation, duckEntity.maxWingDeviation);
        return (MathHelper.sin(g) + 1) * h;
    }
}
