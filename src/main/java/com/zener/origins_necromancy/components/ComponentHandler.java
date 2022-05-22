package com.zener.origins_necromancy.components;

import java.util.ArrayList;
import java.util.List;

import com.zener.origins_necromancy.OriginsNecromancy;
import dev.onyxstudios.cca.api.v3.component.ComponentContainer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import dev.onyxstudios.cca.internal.world.ComponentPersistentState;
import dev.onyxstudios.cca.api.v3.component.ComponentContainer.Factory;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ComponentHandler implements EntityComponentInitializer, WorldComponentInitializer {

    public static final ComponentKey<PhylacteryComponent> PHYLACTERY_KEY = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "phylactery_player"), PhylacteryComponent.class);

    public static final ComponentKey<OwnerUUIDComponent> OWNER_KEY = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "owner"), OwnerUUIDComponent.class);

    public static final ComponentKey<TargetComponent> TARGET_COMPONENT = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "target"), TargetComponent.class);

    public static final ComponentKey<WorldPhylacteryComponent> PHYLACTERY_COMPONENT = ComponentRegistryV3.INSTANCE
        .getOrCreate(new Identifier(OriginsNecromancy.MOD_ID, "phylactery_component"), WorldPhylacteryComponent.class);

    public static ComponentPersistentState PHYLACTERY_PERSISTENCE;
    
    @SuppressWarnings("unchecked")
    public static <C extends ComponentV3> ComponentPersistentState createPersistence(C component) {
        Factory<C> factory = (Factory<C>) Factory.builder(component.getClass()).build();
        ComponentContainer container = factory.createContainer(component);
        return new ComponentPersistentState(container);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(PHYLACTERY_KEY, player -> new PhylacteryComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(TARGET_COMPONENT, player -> new TargetComponent(player), RespawnCopyStrategy.ALWAYS_COPY);
        
        List<Object> added_entities = new ArrayList<Object>();
        Registry.ENTITY_TYPE.getEntries().forEach(e -> {
            Class<? extends Entity> entity = e.getValue().getBaseClass();
            if (!added_entities.contains(entity)) {
                added_entities.add(entity);
                registry.registerFor(entity, OWNER_KEY, _entity -> new OwnerUUIDComponent(_entity));
            }
        });
    }

    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(PHYLACTERY_COMPONENT, world -> {
            WorldPhylacteryComponent worldPhylacteryComponent = new WorldPhylacteryComponent(world);
            PHYLACTERY_PERSISTENCE = createPersistence(worldPhylacteryComponent);
            return worldPhylacteryComponent;
        } );
    }
    
}
