package com.zener.origins_necromancy.commands;

import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zener.origins_necromancy.OriginsNecromancy;

import org.jetbrains.annotations.NotNull;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.Living;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class TargetCommand {
    
    public void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {

        LiteralCommandNode<ServerCommandSource> targetCommandNode = ((LiteralArgumentBuilder<ServerCommandSource>)((LiteralArgumentBuilder<ServerCommandSource>)CommandManager
            .literal("necrotarget")
            .requires(require(OriginsNecromancy.MOD_ID+".necrotarget", 2)))

        ).build();

        dispatcher.getRoot().addChild(targetCommandNode);

    }

    private static @NotNull Predicate<ServerCommandSource> require(String permission, int defaultRequireLevel) {
        return player -> check(player, permission, defaultRequireLevel);
    }

    private static boolean check(@NotNull CommandSource source, @NotNull String permission, int defaultRequireLevel) {
        if (source.hasPermissionLevel(defaultRequireLevel)) { return true; }
        boolean perm = Permissions.getPermissionValue(source, permission).orElse(false);

        if (perm) { return true; } else { return false; }
    }

    private static Entity raycast(ServerPlayerEntity player, Entity entity) {
        Vec3d origin = new Vec3d(entity.getX(), entity.getY(), entity.getZ());
        Vec3d dir = entity.getRotationVec(1);
        Vec3d target = origin.add(dir.multiply(48));

        HitResult result = performEntityRaycast(entity, origin, target);
        if (((EntityHitResult)result).getEntity() != null) {
            return ((EntityHitResult)result).getEntity();
        }
        result = performBlockRaycast(entity, origin, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY);
        double x = result.getPos().x, y = result.getPos().y, z = result.getPos().z;
        Box box = new Box(x-10, y-10, z-10, x+10, y+10, z+10);
        TargetPredicate tPredicate = TargetPredicate.DEFAULT;
        //return player.getServerWorld().getClosestEntity(LivingEntity.class, tPredicate, entity, result.getPos().x, result.getPos().y, result.getPos().z, box);
        return null;
    }

    private static BlockHitResult performBlockRaycast(Entity source, Vec3d origin, Vec3d target, RaycastContext.ShapeType shapeType, RaycastContext.FluidHandling fluidHandling) {
        RaycastContext context = new RaycastContext(origin, target, shapeType, fluidHandling, source);
        return source.world.raycast(context);
    }

    private static EntityHitResult performEntityRaycast(Entity source, Vec3d origin, Vec3d target) {
        Vec3d ray = target.subtract(origin);
        Box box = source.getBoundingBox().stretch(ray).expand(1.0D, 1.0D, 1.0D);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(source, origin, target, box, (entityx) -> {
            return !entityx.isSpectator();
        }, ray.lengthSquared());
        return entityHitResult;
    }

    private class targetPredicate implements Predicate<LivingEntity> {
        
        private final ServerPlayerEntity player;

        targetPredicate(ServerPlayerEntity player) {
            this.player = player;
        }

        @Override
        public boolean test(LivingEntity target) {
            if (target.getUuid().equals(player.getUuid())) return false;
            return true;
        } 
    }

}
