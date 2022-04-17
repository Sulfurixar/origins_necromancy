package com.zener.origins_necromancy;

import com.zener.origins_necromancy.phylactery.PhylacteryCrystalRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;

public class OriginsNecromancyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.register(BlockGen.PHYLACTERY_ENTITY, PhylacteryCrystalRenderer::new);
    }
    
}
