package de.dafuqs.spectrum.blocks.dev;

import com.mojang.serialization.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.*;

public class RenderSandboxBlock extends BlockWithEntity {
	
	private static final MapCodec<RenderSandboxBlock> CODEC = createCodec(RenderSandboxBlock::new);
	
	public RenderSandboxBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() {
		return CODEC;
	}
	
	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RenderSandboxBlockEntity(pos, state);
	}
}
