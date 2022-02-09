package com.zener.origins_necromancy.books;

import com.zener.origins_necromancy.ItemGen;

import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class UseBook {
    
    public static void use(ServerPlayerEntity entity, ItemStack book, Hand hand) {
        for (int i = 0; i < ItemGen.Items.length; i++) {
            if (book.isOf(ItemGen.Items[i])) {
                if (WrittenBookItem.resolve(book, entity.getCommandSource(), entity)) {
                    entity.currentScreenHandler.sendContentUpdates();
                }
                entity.networkHandler.sendPacket(new OpenWrittenBookS2CPacket(hand));
                break;
            }
        }
    }

}
