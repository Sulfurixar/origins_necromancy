package com.zener.origins_necromancy.mixins;

import com.zener.origins_necromancy.books.UseBook;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    
    /*
    @Override
    public void useBook(ItemStack book, Hand hand) {
        if (book.isOf(Items.WRITTEN_BOOK)) {
            if (WrittenBookItem.resolve(book, this.getCommandSource(), this)) {
                this.currentScreenHandler.sendContentUpdates();
            }
            this.networkHandler.sendPacket(new OpenWrittenBookS2CPacket(hand));
        }
    }
    */

    @Inject(method = "useBook(Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)V", at = @At("TAIL"))
    public void useBook(ItemStack book, Hand hand, CallbackInfo ci) {
        ServerPlayerEntity me = ((ServerPlayerEntity)(Object)this);
        UseBook.use(me, book, hand);
    }

}
