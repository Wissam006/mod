package com.wissam006.inferno_mod.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LaserManager {
    private static final Map<UUID, LaserData> ACTIVE_LASERS = new HashMap<>();
    private static final double LASER_REACH = 50.0D;
    private static final double LASER_WIDTH = 0.8D;
    private static final double LASER_DAMAGE_PER_TICK = 10.0D;
    private static final int LASER_TICK_RATE = 2;

    public static class LaserData {
        public Player player;
        public long startTime;
        public int damageTickCounter;

        public LaserData(Player player) {
            this.player = player;
            this.startTime = System.currentTimeMillis();
            this.damageTickCounter = 0;
        }
    }

    public static void startLaser(Player player, Level level) {
        if (level.isClientSide) return;
        
        UUID playerId = player.getUUID();
        if (!ACTIVE_LASERS.containsKey(playerId)) {
            ACTIVE_LASERS.put(playerId, new LaserData(player));
        }
    }

    public static void stopLaser(Player player, Level level) {
        if (level.isClientSide) return;
        ACTIVE_LASERS.remove(player.getUUID());
    }

    public static void updateLasers(Level level) {
        if (level.isClientSide) return;

        ACTIVE_LASERS.values().removeIf(laserData -> laserData.player == null || laserData.player.isDeadOrDying());

        for (LaserData laserData : ACTIVE_LASERS.values()) {
            Player player = laserData.player;
            if (player.level() != level) continue;
            
            Vec3 eyePos = player.getEyePosition(1.0F);
            Vec3 viewDir = player.getViewVector(1.0F);
            Vec3 laserEnd = eyePos.add(viewDir.scale(LASER_REACH));

            AABB laserBox = new AABB(eyePos, laserEnd).inflate(LASER_WIDTH / 2.0D);

            for (Entity entity : level.getEntities(player, laserBox, e -> e instanceof LivingEntity && e != player)) {
                if (entity instanceof LivingEntity target) {
                    laserData.damageTickCounter++;
                    if (laserData.damageTickCounter >= LASER_TICK_RATE) {
                        target.hurt(level.damageSources().playerAttack(player), (float) LASER_DAMAGE_PER_TICK);
                        target.setSecondsOnFire(2);
                        laserData.damageTickCounter = 0;
                    }
                }
            }

            if (level.getGameTime() % 10 == 0) {
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BEACON_AMBIENT, SoundSource.PLAYERS, 0.5F, 1.2F);
            }
        }
    }
}
