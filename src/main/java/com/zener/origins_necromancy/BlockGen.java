package com.zener.origins_necromancy;

import com.zener.origins_necromancy.phylactery.PhylacteryBaseBlock;
import com.zener.origins_necromancy.phylactery.PhylacteryCrystalBlock;
import com.zener.origins_necromancy.phylactery.PhylacteryCrystalRenderer;
import com.zener.origins_necromancy.phylactery.PhylacteryEntity;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockGen {

    public static final Block PHYLACTERY_BASE = new PhylacteryBaseBlock(FabricBlockSettings.of(Material.METAL).strength(4.0f));

    public static final Block PHYLACTERY_CRYSTAL = new PhylacteryCrystalBlock(FabricBlockSettings.of(Material.AMETHYST).strength(4.0f));
    
    public static BlockEntityType<PhylacteryEntity> PHYLACTERY_ENTITY;

    public final static void registerBlocks() {

        Registry.register(Registry.BLOCK, new Identifier(OriginsNecromancy.MOD_ID, "phylactery_base"), PHYLACTERY_BASE);
        Registry.register(Registry.BLOCK, new Identifier(OriginsNecromancy.MOD_ID, "phylactery_crystal"), PHYLACTERY_CRYSTAL);

        Registry.register(Registry.ITEM, new Identifier(OriginsNecromancy.MOD_ID, "phylactery_base"), new BlockItem(PHYLACTERY_BASE, new FabricItemSettings().group(ItemGroup.MISC)));
        Registry.register(Registry.ITEM, new Identifier(OriginsNecromancy.MOD_ID, "phylactery_crystal"), new BlockItem(PHYLACTERY_CRYSTAL, new FabricItemSettings().group(ItemGroup.MISC)));

        PHYLACTERY_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(OriginsNecromancy.MOD_ID, "phylactery"), FabricBlockEntityTypeBuilder.create(PhylacteryEntity::new, PHYLACTERY_CRYSTAL).build(null));
        BlockEntityRendererRegistry.register(PHYLACTERY_ENTITY, PhylacteryCrystalRenderer::new);

        
    }
    
}
