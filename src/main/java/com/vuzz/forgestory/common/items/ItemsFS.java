package com.vuzz.forgestory.common.items;

import com.vuzz.forgestory.ForgeStory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsFS {
    private static String MOD_ID = ForgeStory.MOD_ID;
    private static CreativeModeTab MOD_TAB = ForgeStory.MOD_TAB;
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,MOD_ID);

    public static RegistryObject<Item> NPC_DELETER = ITEMS.register("npc_deleter", () -> new Item(new Item.Properties().fireResistant().stacksTo(1).tab(MOD_TAB)));
    public static RegistryObject<Item> STORY_REFRESHER = ITEMS.register("story_refresher", ModRefresherItem::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
