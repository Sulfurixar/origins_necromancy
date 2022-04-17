package com.zener.origins_necromancy.components;

import com.zener.origins_necromancy.OriginsNecromancy;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public final class ComponentHandler implements EntityComponentInitializer {

    public static final ComponentKey<PhylacteryComponent> PHYLACTERY_KEY = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "phylactery_player"), PhylacteryComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PHYLACTERY_KEY, player -> new PhylacteryComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
    }
    
}
