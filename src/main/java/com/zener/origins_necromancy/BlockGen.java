package com.zener.origins_necromancy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.zener.origins_necromancy.phylactery.PhylacteryBaseBlock;
import com.zener.origins_necromancy.phylactery.PhylacteryCrystalBlock;
import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockGen {

    public static final Block PHYLACTERY_BASE = new PhylacteryBaseBlock(FabricBlockSettings.of(Material.METAL).requiresTool().strength(0.5f));

    public static final Block PHYLACTERY_CRYSTAL = new PhylacteryCrystalBlock(FabricBlockSettings.of(Material.AMETHYST).requiresTool().strength(0.5f));
    
    public static BlockEntityType<PhylacteryEntity> PHYLACTERY_ENTITY;

    private static void registerBlock(String path, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(OriginsNecromancy.MOD_ID, path), block);
        Item item = new BlockItem(block, new FabricItemSettings());
        Registry.register(Registry.ITEM, new Identifier(OriginsNecromancy.MOD_ID, path), item);
        ITEMS.add(item);
    }

    public static final List<Item> ITEMS = new ArrayList<>();

    public final static void registerBlocks() {

        registerBlock("phylactery_base", PHYLACTERY_BASE);
        registerBlock("phylactery_crystal", PHYLACTERY_CRYSTAL);
        registerBlock("bone_block", new _Block(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f)));
        registerBlock("bone_stairs", new Stairs(Blocks.BONE_BLOCK.getDefaultState(), FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f)));
        //registerBlock("bone_tile_stairs", new Stairs(Blocks.BONE_BLOCK.getDefaultState(), FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f)));
        registerBlock("bone_slab", new Slab(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f)));
        registerBlock("bone_tile_slab", new Slab(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f)));
        registerBlock("bone_button", new Button(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).noCollision().strength(0.1f)));
        registerBlock("bone_plate", new PressurePlate(
            PressurePlateBlock.ActivationRule.EVERYTHING, 
            FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().noCollision().strength(0.1f)
        ));
        registerBlock("bone_wall", new Wall(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f).nonOpaque()));
        registerBlock("bone_tile", new _Block(FabricBlockSettings.copyOf(Blocks.BONE_BLOCK).requiresTool().strength(0.1f)));

        OriginsNecromancy.TAB.appendItems(stacks -> stacks.addAll(ITEMS.stream().map(ItemStack::new).collect(Collectors.toList())));

        PHYLACTERY_ENTITY = Registry.register(
            Registry.BLOCK_ENTITY_TYPE, 
            new Identifier(OriginsNecromancy.MOD_ID, "phylactery"), 
            FabricBlockEntityTypeBuilder.create(PhylacteryEntity::new, PHYLACTERY_CRYSTAL).build(null)
        );
    }

    private static class _Block extends Block {

        public _Block(Settings settings) {
            super(settings);
        }

    }

    private static class Stairs extends StairsBlock {

        protected Stairs(BlockState baseBlockState, Settings settings) {
            super(baseBlockState, settings);
        }
        
    }

    private static class Slab extends SlabBlock {

        protected Slab(Settings settings) {
            super(settings);
        }

    }

    private static class Button extends StoneButtonBlock {

        protected Button(Settings settings) {
            super(settings);
        }
        
    }

    private static class PressurePlate extends PressurePlateBlock {

        protected PressurePlate(ActivationRule type, Settings settings) {
            super(type, settings);
        }
        
    }

    private static class Wall extends WallBlock {

        public Wall(Settings settings) {
            super(settings);
        }
        
    }
    
}
