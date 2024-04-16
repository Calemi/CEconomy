package com.calemi.ceconomy.block.entity.renderer;

import com.calemi.ccore.api.render.RenderedFloatingItemStack;
import com.calemi.ceconomy.block.entity.TradingPostBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;

@Environment(EnvType.CLIENT)
public class TradingPostBlockEntityRenderer implements BlockEntityRenderer<TradingPostBlockEntity> {

    private final RenderedFloatingItemStack tradeStack = new RenderedFloatingItemStack();

    public TradingPostBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(TradingPostBlockEntity tradingPost, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        tradeStack.setStack(tradingPost.getTradeStack());

        if (!tradingPost.getTradeStack().isEmpty()) {

            tradeStack.updateSpinningAndFloating();

            matrices.push();
            matrices.translate(0.5F, 0.5F, 0.5F);

            if (tradingPost.getTradeStack().getItem() instanceof BlockItem) {
                matrices.translate(0.0D, -0.1D, 0.0D);
            }

            tradeStack.applyRotations(matrices);
            tradeStack.render(matrices, vertexConsumers, tradingPost.getWorld(), tradingPost.getPos(), light, overlay);
            matrices.pop();
        }
    }
}