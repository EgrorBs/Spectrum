package de.dafuqs.spectrum.worldgen.features;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.*;

public record FluidFloodFeatureConfig(
		ConstantIntProvider startHeight,
		ConstantIntProvider depth,
		IntProvider shoreSize,
		BlockState fillState,
		BlockStateProvider bankState,
		RegistryKey<Biome> biomeTarget
		) implements FeatureConfig {
	
	public static final Codec<FluidFloodFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
			ConstantIntProvider.CODEC.fieldOf("start_height").forGetter(FluidFloodFeatureConfig::startHeight),
			ConstantIntProvider.CODEC.fieldOf("depth").forGetter(FluidFloodFeatureConfig::depth),
			IntProvider.NON_NEGATIVE_CODEC.fieldOf("shore_size").forGetter(FluidFloodFeatureConfig::shoreSize),
			BlockState.CODEC.fieldOf("fill_state").forGetter(FluidFloodFeatureConfig::fillState),
			BlockStateProvider.TYPE_CODEC.fieldOf("bank_states").forGetter(FluidFloodFeatureConfig::bankState),
			RegistryKey.createCodec(RegistryKeys.BIOME).fieldOf("biome").forGetter(FluidFloodFeatureConfig::biomeTarget)
	).apply(instance, FluidFloodFeatureConfig::new));
}
