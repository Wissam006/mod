package com.wissam006.inferno_mod.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import com.wissam006.inferno_mod.util.LaserManager;

public class InfernoLaserBlade extends SwordItem {
    private static final int LASER_COOLDOWN_TICKS = 200;

    public InfernoLaserBlade(ToolMaterial material, Item.Properties properties) {
        super(material, properties.attributes(SwordItem.createAttributes(material, 12.0F, -1.0F)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemStack);
        }

        player.startUsingItem(hand);
        
        if (!level.isClientSide) {
            LaserManager.startLaser(player, level);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.PLAYERS, 1.0F, 0.8F);
        }

        return InteractionResultHolder.success(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            if (!level.isClientSide) {
                LaserManager.stopLaser(player, level);
                player.getCooldowns().addCooldown(this, LASER_COOLDOWN_TICKS);
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide) {
            target.setSecondsOnFire(4);
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
            
            Level level = attacker.level();
            level.explode(null, target.getX(), target.getY() + target.getEyeHeight() / 2, target.getZ(), 
                1.5F, false, net.minecraft.world.level.Level.ExplosionInteraction.NONE);
        }
        stack.hurtAndBreak(1, attacker, attacker instanceof Player p ? p.getUsedItemHand() : InteractionHand.MAIN_HAND);
        return true;
    }
}
