package com.zener.origins_necromancy;

import net.minecraft.item.ItemStack;

public class PageItem extends BookItem{

    public PageItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return false;
    }
}
