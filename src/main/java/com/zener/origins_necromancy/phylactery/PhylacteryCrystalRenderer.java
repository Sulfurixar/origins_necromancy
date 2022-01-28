package com.zener.origins_necromancy.phylactery;

import com.zener.origins_necromancy.BlockGen;
import com.zener.origins_necromancy.ItemGen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class PhylacteryCrystalRenderer implements BlockEntityRenderer<PhylacteryEntity> {

    private static ItemStack charged = new ItemStack(ItemGen.PHYLACTERY_CRYSTAL_ANIM, 1);
    private static ItemStack uncharged = new ItemStack(BlockGen.PHYLACTERY_CRYSTAL.asItem(), 1);

    public PhylacteryCrystalRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(PhylacteryEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        if (blockEntity.has_base) {
            matrices.push();

            // Calculate the current offset in the y value
            double offset = Math.sin((blockEntity.getWorld().getTime() + tickDelta) / 8.0) / 4.0;
            // Move the item
            matrices.translate(0.5, offset, 0.5);
    
            // Rotate the item
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((blockEntity.getWorld().getTime() + tickDelta) * 4));

            int lightAbove = WorldRenderer.getLightmapCoordinates(blockEntity.getWorld(), blockEntity.getPos().up());

            if (blockEntity.getCachedState().get(PhylacteryCrystalBlock.CHARGED).booleanValue()) {
                MinecraftClient.getInstance().getItemRenderer().renderItem(charged, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, blockEntity.hashCode());
            } else {
                MinecraftClient.getInstance().getItemRenderer().renderItem(uncharged, ModelTransformation.Mode.GROUND, lightAbove, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, blockEntity.hashCode());
            }
            matrices.pop();
        }
    }
    
}
