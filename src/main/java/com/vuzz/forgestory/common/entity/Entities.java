package com.vuzz.forgestory.common.entity;

import com.vuzz.forgestory.ForgeStory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Entities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
        = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ForgeStory.MOD_ID);
    
    public static final RegistryObject<EntityType<NPCEntity>> NPC =
        ENTITY_TYPES.register("npc",
            () -> EntityType.Builder.of(NPCEntity::new,
                    MobCategory.AMBIENT).sized(0.8f,1.85f)
                .build(new ResourceLocation(ForgeStory.MOD_ID, "npc").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
