package com.wissam006.inferno_mod.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import com.wissam006.inferno_mod.InfernoMod;
import com.wissam006.inferno_mod.client.renderer.LaserBeamRenderer;
import com.wissam006.inferno_mod.item.InfernoItems;

@Mod.EventBusSubscriber(modid = InfernoMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        
        if (player == null) return;

        if ((player.getMainHandItem().getItem() == InfernoItems.INFERNO_LASER_BLADE.get() ||
             player.getOffhandItem().getItem() == InfernoItems.INFERNO_LASER_BLADE.get()) &&
            player.isUsingItem()) {
            
            LaserBeamRenderer.renderLaserBeam(event.getPoseStack(), mc.renderBuffers().bufferSource(), player, event.getPartialTick());
        }
    }
}
