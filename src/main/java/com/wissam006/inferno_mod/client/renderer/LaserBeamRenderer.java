package com.wissam006.inferno_mod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class LaserBeamRenderer {
    private static final float LASER_WIDTH = 0.8F;
    private static final float LASER_LENGTH = 50.0F;

    public static void renderLaserBeam(PoseStack poseStack, MultiBufferSource buffer, Player player, float partialTick) {
        Vec3 eyePos = player.getEyePosition(partialTick);
        Vec3 viewDir = player.getViewVector(partialTick);
        Vec3 laserEnd = eyePos.add(viewDir.scale(LASER_LENGTH));
        Vec3 cameraPos = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPos();

        poseStack.pushPose();
        poseStack.translate(
            eyePos.x - cameraPos.x,
            eyePos.y - cameraPos.y,
            eyePos.z - cameraPos.z
        );

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.LINES);
        Matrix4f matrix = poseStack.last().pose();

        Vec3 relativeEnd = laserEnd.subtract(eyePos);
        
        drawBeamCore(vertexConsumer, matrix, Vec3.ZERO, relativeEnd);
        drawBeamOuter(vertexConsumer, matrix, Vec3.ZERO, relativeEnd);

        poseStack.popPose();
    }

    private static void drawBeamCore(VertexConsumer consumer, Matrix4f matrix, Vec3 start, Vec3 end) {
        consumer.vertex(matrix, (float) start.x, (float) start.y, (float) start.z)
                .color(1.0F, 1.0F, 0.5F, 1.0F)
                .endVertex();
        consumer.vertex(matrix, (float) end.x, (float) end.y, (float) end.z)
                .color(1.0F, 1.0F, 0.5F, 1.0F)
                .endVertex();
    }

    private static void drawBeamOuter(VertexConsumer consumer, Matrix4f matrix, Vec3 start, Vec3 end) {
        Vec3 offset = new Vec3(LASER_WIDTH / 2, 0, 0);
        consumer.vertex(matrix, (float) (start.x + offset.x), (float) start.y, (float) start.z)
                .color(1.0F, 0.5F, 0.0F, 0.7F)
                .endVertex();
        consumer.vertex(matrix, (float) (end.x + offset.x), (float) end.y, (float) end.z)
                .color(1.0F, 0.5F, 0.0F, 0.7F)
                .endVertex();
        
        offset = new Vec3(0, LASER_WIDTH / 2, 0);
        consumer.vertex(matrix, (float) start.x, (float) (start.y + offset.y), (float) start.z)
                .color(1.0F, 0.5F, 0.0F, 0.7F)
                .endVertex();
        consumer.vertex(matrix, (float) end.x, (float) (end.y + offset.y), (float) end.z)
                .color(1.0F, 0.5F, 0.0F, 0.7F)
                .endVertex();
    }
}
