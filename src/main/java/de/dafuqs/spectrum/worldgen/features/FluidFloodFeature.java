package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.*;

public class FluidFloodFeature extends Feature<FluidFloodFeatureConfig> {
	
	public FluidFloodFeature(Codec<FluidFloodFeatureConfig> configCodec) {
		super(configCodec);
	}
	
	@Override
	public boolean generate(FeatureContext<FluidFloodFeatureConfig> context) {
		var world = context.getWorld();
		var random = context.getRandom();
		var fill = context.getConfig().startHeight().get(random);
		var depth = context.getConfig().depth().get(random);
		var fillState = context.getConfig().fillState();
		var biome = context.getConfig().biomeTarget();
		
		var chunk = world.getChunk(context.getOrigin());
		var origin = chunk.getPos().getStartPos();
		var point = new BlockPos.Mutable();
		
		var shoreWidth = context.getConfig().shoreSize().get(random);
		var bankStates = context.getConfig().bankState();
		
		for (int xOffset = 0; xOffset < 16; xOffset++) {
			point.setX(origin.getX() + xOffset);
			
			for (int zOffset = 0; zOffset < 16; zOffset++) {
				point.setZ(origin.getZ() + zOffset);
				
				for (int yOffset = 0; yOffset <= depth; yOffset++) {
					point.setY(fill - yOffset);
					
					if (!world.getBiome(point).matchesKey(biome))
						continue;
					
					if (shouldRemove(world.getBlockState(point))) {
						var placedState = fillState;
						
						if (yOffset == depth) {
							placedState = SpectrumBlocks.BLACKSLAG.getDefaultState();
						}
						else if (isOnBiomeBorder(world, biome, point)) {
							placedState =  bankStates.get(random, point);
						}
						
						setBlockState(world, point, placedState);

						if (yOffset == 0) {
							point.move(Direction.UP);
							setBlockStateIf(world, point, Blocks.AIR.getDefaultState(), FluidFloodFeature::shouldRemove);
						}
					}
					else if (world.getBlockState(point.up()).isOf(fillState.getBlock())) {
						setBlockState(world, point, SpectrumBlocks.BLACKSLAG.getDefaultState());
					}
					else if (isShore(world, shoreWidth, fillState, point)){
						setBlockStateIf(world, point, bankStates.get(random, point), s -> s.isIn(SpectrumBlockTags.DEEPER_DOWN_FLOODABLES));
					}
				}
			}
		}
		
		return true;
	}

	private static boolean shouldRemove(BlockState state) {
		return state.isReplaceable() || state.isIn(SpectrumBlockTags.DEEPER_DOWN_FLOOD_REPLACEABLES);
	}

	private static boolean isOnBiomeBorder(StructureWorldAccess world, RegistryKey<Biome> key, BlockPos center) {
		BlockPos.Mutable mutable = center.mutableCopy();
		
		for (Direction direction : Direction.values()) {
			if (direction.getHorizontal() == -1)
				continue;
			
			mutable.set(center, direction);
			if (!world.getBiome(mutable).matchesKey(key)) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isShore(StructureWorldAccess world, int width, BlockState fillState, BlockPos center) {
		BlockPos.Mutable mutable = center.mutableCopy();
		
		if (width < 1)
			return false;
		
		if (!world.isAir(mutable.set(center, Direction.UP)))
			return false;
		
		for (Direction direction : Direction.values()) {
			if (direction.getHorizontal() == -1)
				continue;
			
			for (int i = 1; i <= width; i++) {
				mutable.set(center).move(direction, i);
				if (world.getBlockState(mutable).isOf(fillState.getBlock())) {
					return true;
				}
			}
		}
		return false;
	}
}
