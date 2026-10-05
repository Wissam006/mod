package com.wissam006.inferno_mod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wissam006.inferno_mod.item.InfernoItems;

@Mod(InfernoMod.MODID)
public class InfernoMod {
    public static final String MODID = "inferno_mod";
    private static final Logger LOGGER = LoggerFactory.getLogger(InfernoMod.class);

    public InfernoMod(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Initializing Inferno Mod...");
        
        InfernoItems.ITEMS.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Common setup completed for Inferno Mod");
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        LOGGER.info("Client setup completed for Inferno Mod");
    }
}
