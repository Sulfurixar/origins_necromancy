package com.zener.origins_necromancy.commands;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.components.ComponentHandler;
import com.zener.origins_necromancy.components.TargetComponent;

import org.jetbrains.annotations.NotNull;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

public class TargetCommand {

    public static TargetPredicate predicate = TargetPredicate.createNonAttackable().setBaseMaxDistance(32);
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {

        LiteralCommandNode<ServerCommandSource> targetCommandNode = ((LiteralArgumentBuilder<ServerCommandSource>)((LiteralArgumentBuilder<ServerCommandSource>)CommandManager
            .literal("necrotarget")
            .requires(require(OriginsNecromancy.MOD_ID+".necrotarget", 2)))
            .executes(context -> runTarget(context.getSource(), context.getSource().getPlayer()))
        ).build();

        dispatcher.getRoot().addChild(targetCommandNode);

    }

    private static int runTarget(ServerCommandSource source, ServerPlayerEntity player) {
        if (player == null) { source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".necrotarget.no_player"), false); return 0; }
        LivingEntity target = raycast(player);
        if (target == null) { source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".necrotarget.no_target"), false); return 0; }
        TargetComponent targetComponent = ComponentHandler.TARGET_COMPONENT.get(player);
        targetComponent.setTarget(target);
        ComponentHandler.TARGET_COMPONENT.sync(player);
        return Command.SINGLE_SUCCESS;
    }

    private static @NotNull Predicate<ServerCommandSource> require(String permission, int defaultRequireLevel) {
        return player -> check(player, permission, defaultRequireLevel);
    }

    private static boolean check(@NotNull CommandSource source, @NotNull String permission, int defaultRequireLevel) {
        if (source.hasPermissionLevel(defaultRequireLevel)) { return true; }
        boolean perm = Permissions.getPermissionValue(source, permission).orElse(false);

        if (perm) { return true; } else { return false; }
    }

    @Nullable
    private static LivingEntity raycast(ServerPlayerEntity player) {
        Vec3d origin = new Vec3d(player.getX(), player.getY()+player.getEyeHeight(player.getPose()), player.getZ());
        Vec3d dir = player.getRotationVec(1);
        Vec3d target = origin.add(dir.multiply(48));
        
        try {
            HitResult result = performEntityRaycast(player, origin, target);
            if (((EntityHitResult)result) != null && ((EntityHitResult)result).getEntity() != null) {
                return (LivingEntity)((EntityHitResult)result).getEntity();
            }
            result = performBlockRaycast(player, origin, target, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY);
            double x = result.getPos().x, y = result.getPos().y, z = result.getPos().z;
            Box box = new Box(x-10, y-10, z-10, x+10, y+10, z+10);
            LivingEntity _target = player.world.getClosestEntity(LivingEntity.class, predicate, player, result.getPos().x, result.getPos().y, result.getPos().z, box);
            return _target;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
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

}
