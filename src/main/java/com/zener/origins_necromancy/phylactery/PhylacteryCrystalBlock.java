package com.zener.origins_necromancy.phylactery;

import com.zener.origins_necromancy.BlockGen;
import com.zener.origins_necromancy.OriginsNecromancy;
import com.zener.origins_necromancy.TranslatedTexts;
import com.zener.origins_necromancy.components.ComponentHandler;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PhylacteryCrystalBlock extends BlockWithEntity {

    public static final BooleanProperty ON_BASE = BooleanProperty.of("b");
    public static final BooleanProperty CHARGED = BooleanProperty.of("c");

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.375f, 0f, 0.375f, 0.625f, 0.5f, 0.625f);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(ON_BASE);
        stateManager.add(CHARGED);
    }

    public PhylacteryCrystalBlock(Settings settings) {
        super(settings.nonOpaque());
        setDefaultState(getStateManager().getDefaultState().with(ON_BASE, false));
        setDefaultState(getStateManager().getDefaultState().with(CHARGED, false));
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PhylacteryEntity(pos, state);
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> checkType(BlockEntityType<A> giveType, BlockEntityType<E> expectedType, BlockEntityTicker<? super E> ticker) {
        return expectedType == giveType ? (@Nullable BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!state.get(ON_BASE).booleanValue()) return ActionResult.SUCCESS;

        if (player.isSneaking()) {
            if (state.get(CHARGED).booleanValue()) {
                world.setBlockState(pos, state.with(CHARGED, false));
                BlockEntity entity = world.getBlockEntity(pos);
                if (entity instanceof PhylacteryEntity) {
                    PhylacteryEntity e = (PhylacteryEntity) entity;
                    e.uuid = null;
                }
            }
            return ActionResult.SUCCESS;
        }

        if (player.experienceLevel >= 40) {
            if (state.get(CHARGED)) {
                player.sendMessage(TranslatedTexts.ALREADY_CHARGED, true);
                return ActionResult.SUCCESS;
            }
            player.experienceLevel = player.experienceLevel - 40;
            world.setBlockState(pos, state.with(CHARGED, true));
            BlockEntity entity = world.getBlockEntity(pos);
            if (entity instanceof PhylacteryEntity) {
                PhylacteryEntity e = (PhylacteryEntity) entity;
                e.uuid = player.getUuid();
                if (!world.isClient) {
                    System.out.println(OriginsNecromancy.PHYLACTERY_ADVANCEMENT.getId().toString());
                    ((ServerPlayerEntity)player).getAdvancementTracker().grantCriterion(OriginsNecromancy.PHYLACTERY_ADVANCEMENT, "command");
                    ((ServerPlayerEntity)player).getAdvancementTracker().revokeCriterion(OriginsNecromancy.PHYLACTERY_ADVANCEMENT, "command");
                    ComponentHandler.PHYLACTERY_KEY.get(player).setPhylactery(e);
                    ComponentHandler.PHYLACTERY_KEY.get(player).setPlayer();
                }
            }

            return ActionResult.SUCCESS;
        } else {
            player.sendMessage(TranslatedTexts.LOW_LEVELS, true);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, BlockGen.PHYLACTERY_ENTITY, (_world, pos, _state, be) -> PhylacteryEntity.serverTick(_world, pos, _state, be));
    }

}
