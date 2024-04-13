package dev.creoii.greatbigworld.floraandfauna.client.model;

import com.google.common.collect.ImmutableList;
import dev.creoii.greatbigworld.floraandfauna.entity.DuckEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class DuckEntityModel extends AnimalModel<DuckEntity> {
    private final ModelPart head;
    private final ModelPart bill;
    private final ModelPart body;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public DuckEntityModel(ModelPart root) {
        super(false, 4.9f, 2f);
        head = root.getChild("head");
        bill = root.getChild("bill");
        body = root.getChild("body");
        leftWing = root.getChild("left_wing");
        rightWing = root.getChild("right_wing");
        leftLeg = root.getChild("left_leg");
        rightLeg = root.getChild("right_leg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
                                                                                                                                                                // here
        ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create().uv(14, 14).cuboid(-2.0F, -6.0F, -1.0F, 4.0F, 6.0F, 3.0F), ModelTransform.pivot(0.0F, 15.0F, -4.0F));
        ModelPartData bill = modelPartData.addChild("bill", ModelPartBuilder.create().uv(18, 0).cuboid(-2.0F, -4.0F, -4.0F, 4.0F, 2.0F, 3.0F), ModelTransform.pivot(0.0F, 15.0F, -4.0F));
        ModelPartData body = modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -4.0F, -4.0F, 6.0F, 8.0F, 6.0F), ModelTransform.of(0.0F, 16.0F, 0.0F, 1.5708F, 0.0F, 0.0f));

        ModelPartData leftWing = modelPartData.addChild("left_wing", ModelPartBuilder.create().uv(0, 14).cuboid(0.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), ModelTransform.pivot(3.0F, 14.0F, 0.0F));
        ModelPartData rightWing = modelPartData.addChild("right_wing", ModelPartBuilder.create().uv(0, 14).cuboid(-1.0F, 0.0F, -3.0F, 1.0F, 4.0F, 6.0F), ModelTransform.pivot(-3.0F, 14.0F, 0.0F));

        ModelPartData leftLeg = modelPartData.addChild("left_leg", ModelPartBuilder.create().uv(11, 23).cuboid(-1.0F, 1.0F, -3.0F, 3.0F, 4.0F, 3.0F), ModelTransform.pivot(1.5F, 20.0F, 1.0F));
        ModelPartData rightLeg = modelPartData.addChild("right_leg", ModelPartBuilder.create().uv(11, 23).cuboid(-1.0F, 1.0F, -3.0F, 3.0F, 4.0F, 3.0F), ModelTransform.pivot(-1.5F, 20.0F, 1.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void animateModel(DuckEntity entity, float limbAngle, float limbDistance, float tickDelta) {
    }

    @Override
    public void setAngles(DuckEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        head.pitch = headPitch * ((float) Math.PI / 180f);
        head.yaw = headYaw * ((float) Math.PI / 180f);
        bill.pitch = head.pitch;
        bill.yaw = head.yaw;
        rightLeg.pitch = MathHelper.cos(limbAngle * .6662f) * 1.4f * limbDistance;
        leftLeg.pitch = MathHelper.cos(limbAngle * .6662f + (float) Math.PI) * 1.4f * limbDistance;
        if (!entity.isTouchingWater()) {
            rightWing.roll = animationProgress;
            leftWing.roll = -animationProgress;

            leftLeg.visible = true;
            rightLeg.visible = true;
        } else {
            rightWing.roll *= .05f;
            leftWing.roll *= .05f;

            if (!entity.getVelocity().equals(Vec3d.ZERO) || entity.getMoveControl().isMoving()) {
                leftLeg.visible = false;
                rightLeg.visible = false;

                if (!entity.getLookControl().isLookingAtSpecificPosition()) {
                    float bob = MathHelper.sin(entity.age * .4f) * .1f;
                    head.pitch = bob * 1.75f + .3334f;
                    bill.pitch = head.pitch;
                }
            }
        }
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(head, bill);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(body, rightLeg, leftLeg, rightWing, leftWing);
    }
}
