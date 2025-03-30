package de.dafuqs.spectrum.blocks.dev;

import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.util.math.*;

public class RenderSandboxBlockEntity extends BlockEntity {
	
	public RenderSandboxBlockEntity(BlockPos pos, BlockState state) {
		super(SpectrumBlockEntities.RENDER_SANDBOX, pos, state);
	}
}
