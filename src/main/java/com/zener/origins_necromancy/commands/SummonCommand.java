package com.zener.origins_necromancy.commands;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.components.ComponentHandler;

import org.jetbrains.annotations.NotNull;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.EntitySummonArgumentType;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SummonCommand {
    private static final SimpleCommandExceptionType FAILED_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText(OriginsNecromancy.MOD_ID+".summon.failed"));
    private static final SimpleCommandExceptionType FAILED_UUID_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText(OriginsNecromancy.MOD_ID+".summon.failed.uuid"));
    private static final SimpleCommandExceptionType INVALID_POSITION_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText(OriginsNecromancy.MOD_ID+".summon.invalidPosition"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {

        LiteralCommandNode<ServerCommandSource> summonCommandNode = ((LiteralArgumentBuilder<ServerCommandSource>)((LiteralArgumentBuilder<ServerCommandSource>)CommandManager
            .literal("necrosummon")
            .requires(require(OriginsNecromancy.MOD_ID+".necrosummon", 2)))
            .then(
                (RequiredArgumentBuilder<ServerCommandSource, Identifier>)CommandManager.argument("entity", EntitySummonArgumentType.entitySummon())
                .suggests(SuggestionProviders.SUMMONABLE_ENTITIES)
                .executes(context -> execute(
                    context.getSource(),
                    EntitySummonArgumentType.getEntitySummon(context, "entity"),
                    context.getSource().getPosition(),
                    new NbtCompound(),
                    true,
                    null
                ))
                .then(
                    (RequiredArgumentBuilder<ServerCommandSource,PosArgument>)CommandManager.argument("pos", Vec3ArgumentType.vec3())
                    .executes(context -> execute(
                        context.getSource(),
                        EntitySummonArgumentType.getEntitySummon(context, "entity"),
                        Vec3ArgumentType.getVec3(context, "pos"),
                        new NbtCompound(),
                        true,
                        null
                    ))
                    .then(
                        CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound())
                        .executes(context -> execute(
                            context.getSource(),
                            EntitySummonArgumentType.getEntitySummon(context, "entity"),
                            Vec3ArgumentType.getVec3(context, "pos"),
                            NbtCompoundArgumentType.getNbtCompound(context, "nbt"),
                            false,
                            null
                        ))
                        .then(
                            CommandManager.literal("for")
                            .then(
                                CommandManager.argument("player", EntityArgumentType.player())
                                .executes(context -> execute(
                                    context.getSource(),
                                    EntitySummonArgumentType.getEntitySummon(context, "entity"),
                                    Vec3ArgumentType.getVec3(context, "pos"),
                                    NbtCompoundArgumentType.getNbtCompound(context, "nbt"),
                                    false,
                                    EntityArgumentType.getPlayer(context, "player")
                                ))
                            )
                        )
                    )
                )
            )
        ).build();
        
        dispatcher.getRoot().addChild(summonCommandNode);

        
        /*(LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("summon").requires(source -> source.hasPermissionLevel(2)))
        .then(((RequiredArgumentBuilder)CommandManager.argument("entity", EntitySummonArgumentType.entitySummon())
        .suggests(SuggestionProviders.SUMMONABLE_ENTITIES).executes(context -> 
            SummonCommand.execute((ServerCommandSource)context.getSource(), EntitySummonArgumentType.getEntitySummon(context, "entity"), 
            ((ServerCommandSource)context.getSource()).getPosition(), new NbtCompound(), true)))
            .then(((RequiredArgumentBuilder)CommandManager.argument("pos", Vec3ArgumentType.vec3()).executes(context -> 
            SummonCommand.execute((ServerCommandSource)context.getSource(), EntitySummonArgumentType.getEntitySummon(context, "entity"), Vec3ArgumentType.getVec3(context, "pos"), new NbtCompound(), true)))
            .then(CommandManager.argument("nbt", NbtCompoundArgumentType.nbtCompound()).executes(context -> SummonCommand.execute((ServerCommandSource)context.getSource(), 
            EntitySummonArgumentType.getEntitySummon(context, "entity"), Vec3ArgumentType.getVec3(context, "pos"), NbtCompoundArgumentType.getNbtCompound(context, "nbt"), false)))))
        */
    }

    private static int execute(ServerCommandSource source, Identifier entity2, Vec3d pos, NbtCompound nbt, boolean initialize, @Nullable PlayerEntity player) throws CommandSyntaxException {
        BlockPos blockPos = new BlockPos(pos);
        if (!World.isValid(blockPos)) {
            throw INVALID_POSITION_EXCEPTION.create();
        }
        NbtCompound nbtCompound = nbt.copy();
        nbtCompound.putString("id", entity2.toString());
        ServerWorld serverWorld = source.getWorld();
        Entity entity22 = EntityType.loadEntityWithPassengers(nbtCompound, serverWorld, entity -> {
            entity.refreshPositionAndAngles(pos.x, pos.y, pos.z, entity.getYaw(), entity.getPitch());
            return entity;
        });
        if (entity22 == null) {
            throw FAILED_EXCEPTION.create();
        }
        if (initialize && entity22 instanceof MobEntity) {
            ((MobEntity)entity22).initialize(source.getWorld(), source.getWorld().getLocalDifficulty(entity22.getBlockPos()), SpawnReason.COMMAND, null, null);
        }
        if (!serverWorld.shouldCreateNewEntityWithPassenger(entity22)) {
            throw FAILED_UUID_EXCEPTION.create();
        }
        if (player != null) {
            ComponentHandler.OWNER_KEY.get(entity22).setOwner(player);
        }
        source.sendFeedback(new TranslatableText(OriginsNecromancy.MOD_ID+".summon.success", entity22.getDisplayName()), true);
        return 1;
    }

    private static @NotNull Predicate<ServerCommandSource> require(String permission, int defaultRequireLevel) {
        return player -> check(player, permission, defaultRequireLevel);
    }

    private static boolean check(@NotNull CommandSource source, @NotNull String permission, int defaultRequireLevel) {
        if (source.hasPermissionLevel(defaultRequireLevel)) { return true; }
        boolean perm = Permissions.getPermissionValue(source, permission).orElse(false);

        if (perm) { return true; } else { return false; }
    }
}
