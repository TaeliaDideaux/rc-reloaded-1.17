package com.TaeliaDideaux.rcreloaded.core.world;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.TaeliaDideaux.rcreloaded.RatchetAndClankReloadedMod;
import com.TaeliaDideaux.rcreloaded.core.init.BlockInit;
import com.google.common.collect.ImmutableList;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

public class OreGeneration {
	
	public static final List<ConfiguredFeature<?, ?>> OVERWORLD_ORES = new ArrayList<>();
	public static final List<ConfiguredFeature<?, ?>> NETHER_ORES = new ArrayList<>();
	
	public static void registerOres() {
		
		final ImmutableList<OreConfiguration.TargetBlockState> RARITANIUM_ORE_TARGET_LIST = ImmutableList.of(
				OreConfiguration.target(OreConfiguration.Predicates.STONE_ORE_REPLACEABLES, BlockInit.RARITANIUM_ORE.get().defaultBlockState()),
				OreConfiguration.target(OreConfiguration.Predicates.DEEPSLATE_ORE_REPLACEABLES, BlockInit.DEEPSLATE_RARITANIUM_ORE.get().defaultBlockState()));
		final ConfiguredFeature<?, ?> RARITANIUM_ORE = register( "raritanium_ore", Feature.ORE.configured(new OreConfiguration(RARITANIUM_ORE_TARGET_LIST, 4)).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)).squared().count(4));

		OVERWORLD_ORES.add(RARITANIUM_ORE);
		

		
		final ImmutableList<OreConfiguration.TargetBlockState> ADAMANTINE_ORE_TARGET_LIST = ImmutableList.of(
				OreConfiguration.target(OreConfiguration.Predicates.NETHER_ORE_REPLACEABLES, BlockInit.ADAMANTINE_ORE.get().defaultBlockState()));
		final ConfiguredFeature<?, ?> ADAMANTINE_ORE = register( "adamantine_ore", Feature.ORE.configured(new OreConfiguration(ADAMANTINE_ORE_TARGET_LIST, 6)).rangeUniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)).squared().count(6));

		NETHER_ORES.add(ADAMANTINE_ORE);
		
	}
	
	private static <Config extends FeatureConfiguration> ConfiguredFeature<Config, ?> register(String name, ConfiguredFeature<Config, ?> configuredFeature) {
		return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, new ResourceLocation(RatchetAndClankReloadedMod.MODID, name), configuredFeature);
	}
	

	@Mod.EventBusSubscriber(modid = RatchetAndClankReloadedMod.MODID, bus = Bus.FORGE)
	public static class ForgeBusSubscriber {
		
		@SubscribeEvent
		public static void biomeLoading(BiomeLoadingEvent event) {
			List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration().getFeatures(Decoration.UNDERGROUND_ORES);
			switch (event.getCategory()) {
				case NETHER -> OreGeneration.NETHER_ORES.forEach(ore -> features.add(()->ore));
				default -> OreGeneration.OVERWORLD_ORES.forEach(ore -> features.add(()->ore));
			}
		}
	}
}
