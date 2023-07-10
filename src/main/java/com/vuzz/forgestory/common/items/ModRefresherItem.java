package com.vuzz.forgestory.common.items;

import com.vuzz.forgestory.ForgeStory;
import com.vuzz.forgestory.api.plotter.story.Root;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class ModRefresherItem extends Item {

    public ModRefresherItem() {
        super(
                new Item.Properties()
                        .fireResistant()
                        .rarity(Rarity.EPIC)
                        .stacksTo(1)
                        .tab(ForgeStory.MOD_TAB)
        );
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        if(!level.isClientSide) {
            Root.reloadStories();
            return InteractionResult.PASS;
        }
        return super.onItemUseFirst(stack,context);
    }
}