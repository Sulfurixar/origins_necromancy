package com.zener.origins_necromancy.components;

import java.util.ArrayList;
import java.util.List;

import com.zener.origins_necromancy.OriginsNecromancy;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ComponentHandler implements EntityComponentInitializer {

    public static final ComponentKey<PhylacteryComponent> PHYLACTERY_KEY = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "phylactery_player"), PhylacteryComponent.class);

    public static final ComponentKey<OwnerUUIDComponent> OWNER_KEY = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "owner"), OwnerUUIDComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PHYLACTERY_KEY, player -> new PhylacteryComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
        
        List<Object> added_entities = new ArrayList<Object>();
        Registry.ENTITY_TYPE.getEntries().forEach(e -> {
            Class<? extends Entity> entity = e.getValue().getBaseClass();
            if (!added_entities.contains(entity)) {
                added_entities.add(entity);
                registry.registerFor(entity, OWNER_KEY, _entity -> new OwnerUUIDComponent(_entity));
            }
        });
    }
    
}
