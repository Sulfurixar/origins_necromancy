package com.zener.origins_necromancy.mixins;

import com.zener.origins_necromancy.ItemGen;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OpenWrittenBookS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    
    /*
    @Override
    public void onOpenWrittenBook(OpenWrittenBookS2CPacket packet) {
        NetworkThreadUtils.forceMainThread(packet, this, this.client);
        ItemStack itemStack = this.client.player.getStackInHand(packet.getHand());
        if (itemStack.isOf(Items.WRITTEN_BOOK)) {
            this.client.setScreen(new BookScreen(new BookScreen.WrittenBookContents(itemStack)));
        }
    }
    */

    @Shadow
    @Final
    private MinecraftClient client;

    @Inject(method = "onOpenWrittenBook(Lnet/minecraft/network/packet/s2c/play/OpenWrittenBookS2CPacket;)V", at = @At("TAIL"))
    public void onOpenWrittenBook(OpenWrittenBookS2CPacket packet, CallbackInfo ci) {
        ItemStack itemStack = client.player.getStackInHand(packet.getHand());
        for (int i = 0; i < ItemGen.Items.length; i++) {
            if (itemStack.isOf(ItemGen.Items[i])) {
                client.setScreen(new BookScreen(new BookScreen.WrittenBookContents(itemStack)));
                break;
            }
        }
    }

}
