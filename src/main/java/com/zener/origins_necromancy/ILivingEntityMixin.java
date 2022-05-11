package com.zener.origins_necromancy;

import net.minecraft.item.ItemStack;

public interface ILivingEntityMixin {
    
    public ItemStack varItemStack = ItemStack.EMPTY;
    public ItemStack getVarItemStack();
    public void setVarItemStack(ItemStack stack);

}
