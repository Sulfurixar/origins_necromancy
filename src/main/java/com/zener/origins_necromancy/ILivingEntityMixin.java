package com.zener.origins_necromancy;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;

public interface ILivingEntityMixin {
    
    @Getter @Setter public ItemStack varItemStack = ItemStack.EMPTY;

}
