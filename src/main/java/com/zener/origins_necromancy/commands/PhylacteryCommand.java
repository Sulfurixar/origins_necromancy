package com.zener.origins_necromancy.commands;

import java.util.Iterator;
import java.util.UUID;
import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.components.ComponentHandler;
import com.zener.origins_necromancy.components.PhylacteryComponent;
import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import org.jetbrains.annotations.NotNull;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class PhylacteryCommand {

    private static @NotNull Predicate<ServerCommandSource> require(String permission, int defaultRequireLevel) {
        return player -> check(player, permission, defaultRequireLevel);
    }

    private static boolean check(@NotNull CommandSource source, @NotNull String permission, int defaultRequireLevel) {
        if (source.hasPermissionLevel(defaultRequireLevel)) { return true; }
        boolean perm = Permissions.getPermissionValue(source, permission).orElse(false);

        if (perm) { return true; } else { return false; }
    }

    private static PhylacteryEntity findPhylactery(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerCommandSource source = ctx.getSource();
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".not_player"), true);
            return null;
        }

        PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
        BlockPos phylacteryPos = new BlockPos(component.phylacteryX(), component.phylacteryY(), component.phylacteryZ());
        Iterator<ServerWorld> iterator = ((ServerPlayerEntity)player).server.getWorlds().iterator();
        while (iterator.hasNext()) {
            ServerWorld world = iterator.next();
            if (world.getDimension().getSuffix().equals(component.world())) {
                BlockEntity blockEntity = world.getBlockEntity(phylacteryPos);
                if (!(blockEntity instanceof PhylacteryEntity)) {
                    source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".no_phylactery"), false);
                    return null;
                }
                return ((PhylacteryEntity) blockEntity);
            }
        }

        source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".world_not_found"), false);
        return null;
    }
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralCommandNode<ServerCommandSource> phylacteryRoot = CommandManager.literal("phylactery").executes(ctx -> {
            ServerCommandSource source = ctx.getSource();
            ServerPlayerEntity player = source.getPlayer();
            
            PhylacteryEntity phylacteryEntity = findPhylactery(ctx);
            if (phylacteryEntity == null) return 0;
            
            UUID phylacteryUUID = phylacteryEntity.uuid;
            UUID playerUUID = player.getUuid();
            if (phylacteryUUID == null || playerUUID == null || phylacteryUUID.compareTo(playerUUID) != 0) {
                source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".not_bound"), false);
                return 0;
            }
            source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".found"), false);
            return 1;
        }).build();

        LiteralCommandNode<ServerCommandSource> phylacteryRootTeleport = CommandManager.literal("teleport")
        .requires(require(OriginsNecromancy.MOD_ID+"teleport", 2))
        .executes(ctx -> {
            ServerCommandSource source = ctx.getSource();
            ServerPlayerEntity player = source.getPlayer();

            PhylacteryEntity phylacteryEntity = findPhylactery(ctx);
            if (phylacteryEntity == null) return 0;
            
            UUID phylacteryUUID = phylacteryEntity.uuid;
            UUID playerUUID = player.getUuid();
            if (phylacteryUUID == null || playerUUID == null || phylacteryUUID.compareTo(playerUUID) != 0) {
                source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".not_bound"), false);
                return 0;
            }
            source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".found"), false);

            // ADD MORE STUFF
            
            PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
            PhylacteryEntity.playerRespawn(player, phylacteryEntity, component);
            return 1;
        }).build();

        phylacteryRoot.addChild(phylacteryRootTeleport);

        dispatcher.getRoot().addChild(phylacteryRoot);
    }

}
