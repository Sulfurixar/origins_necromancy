package com.zener.origins_necromancy.phylactery;

import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.components.ComponentHandler;
import com.zener.origins_necromancy.components.PhylacteryComponent;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class PhylacteryCommand {
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        LiteralCommandNode<ServerCommandSource> phylacteryRoot = CommandManager.literal("phylactery").executes(ctx -> {
            ServerCommandSource source = ctx.getSource();
            ServerPlayerEntity player = source.getPlayer();
            if (player == null) {
                source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".not_player"), true);
                return 0;
            }

            PhylacteryComponent component = ComponentHandler.PHYLACTERY_KEY.get(player);
            BlockPos phylacteryPos = new BlockPos(component.phylacteryX(), component.phylacteryY(), component.phylacteryZ());
            BlockEntity blockEntity = player.world.getBlockEntity(phylacteryPos);
            if (!(blockEntity instanceof PhylacteryEntity)) {
                source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".no_phylactery"), false);
                return 0;
            }

            PhylacteryEntity phylacteryEntity = (PhylacteryEntity)blockEntity;
            
            UUID phylacteryUUID = phylacteryEntity.uuid;
            UUID playerUUID = player.getUuid();
            if (phylacteryUUID == null || playerUUID == null || phylacteryUUID.compareTo(playerUUID) != 0) {
                source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".not_bound"), false);
                return 0;
            }
            source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".found"), false);
            return 1;
        }).build();
        dispatcher.getRoot().addChild(phylacteryRoot);
    }

}
