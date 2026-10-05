package com.wissam006.inferno_mod.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import com.wissam006.inferno_mod.InfernoMod;
import com.wissam006.inferno_mod.util.LaserManager;

@Mod.EventBusSubscriber(modid = InfernoMod.MODID)
public class CommonEvents {
    
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        event.getServer().getAllLevels().forEach(level -> {
            LaserManager.updateLasers(level);
        });
    }
}
