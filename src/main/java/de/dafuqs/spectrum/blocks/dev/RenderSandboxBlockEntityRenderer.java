package de.dafuqs.spectrum.blocks.dev;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;

public class RenderSandboxBlockEntityRenderer implements BlockEntityRenderer<RenderSandboxBlockEntity> {
	
	public RenderSandboxBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {}
	
	@Override
	public void render(RenderSandboxBlockEntity sandbox, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		var ref = sandbox.getPos().toCenterPos();
		var renderer = MinecraftClient.getInstance().getBlockRenderManager();
		var deflectionX = 15;
		var time = sandbox.getWorld().getTime() % 1000000 + tickDelta;
		
		Catenary cat = new Catenary(new Vec2f((float) -deflectionX, (float) ((Math.cos(time / 30) + 2) * 10)), new Vec2f((float) deflectionX, (float) ((Math.sin((time - 5) / 30) + 2) * 10)), 12);
		
		for (double i = -deflectionX; i <= deflectionX; i += 0.5) {
			Vec2f point = new Vec2f((float) i, (float) cat.getAt(i));
			
			matrices.push();
			
			matrices.translate(point.x, point.y, 0);
			var pre = new Vec2f((float) i - 0.05F, (float) cat.getAt(i - 0.05F));
			var post = new Vec2f((float) i + 0.05F, (float) cat.getAt(i + 0.05F));
			var floor = post.x - pre.x;
			var height = post.y - pre.y;
			var h = Math.sqrt(Math.pow(floor, 2) + Math.pow(height, 2));
			var angle = Math.asin(height / h);
			
			matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) angle));
			renderer.renderBlockAsEntity(SpectrumBlocks.PRIMORDIAL_FIRE.getDefaultState(), matrices, vertexConsumers, light, overlay);
			
			matrices.pop();
		}
		
		matrices.push();
		
		renderer.renderBlockAsEntity(Blocks.SEA_LANTERN.getDefaultState(), matrices, vertexConsumers, light, overlay);
		
		matrices.pop();
	}
	
	@Override
	public boolean rendersOutsideBoundingBox(RenderSandboxBlockEntity blockEntity) {
		return true;
	}
	
	@Override
	public int getRenderDistance() {
		return 128;
	}
}
