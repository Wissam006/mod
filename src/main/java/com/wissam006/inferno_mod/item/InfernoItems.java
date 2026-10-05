package com.wissam006.inferno_mod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

import com.wissam006.inferno_mod.InfernoMod;

public class InfernoItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, InfernoMod.MODID);

    public static final net.neoforged.neoforge.registries.DeferredHolder<Item, InfernoLaserBlade> INFERNO_LASER_BLADE = 
        ITEMS.register("inferno_laser_blade", () -> new InfernoLaserBlade(Tiers.NETHERITE, new Item.Properties()));
}
