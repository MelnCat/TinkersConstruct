package slimeknights.tconstruct.common.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import slimeknights.tconstruct.TConstruct;

import java.util.List;

import static slimeknights.tconstruct.common.structure.ConfiguredFeatures.*;

public class PlacedFeatures {
  public static final ResourceKey<PlacedFeature> placedEarthGeode = key("earth_geode");
  public static final ResourceKey<PlacedFeature> placedSkyGeode = key("sky_geode");
  public static final ResourceKey<PlacedFeature> placedIchorGeode = key("ichor_geode");
  public static final ResourceKey<PlacedFeature> placedEnderGeode = key("ender_geode");

  public static final ResourceKey<PlacedFeature> placedSmallCobaltOre = key("placed_cobalt_ore_small");
  public static final ResourceKey<PlacedFeature> placedLargeCobaltOre = key("placed_cobalt_ore_large");


  public static void generatePlacedFeatures(BootstapContext<PlacedFeature> context) {
    var configuredFeatureRegistry = context.lookup(Registries.CONFIGURED_FEATURE);

    context.register(placedEarthGeode, new PlacedFeature(
      configuredFeatureRegistry.getOrThrow(configuredEarthGeode),
      List.of(RarityFilter.onAverageOnceEvery(128), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.aboveBottom(54)))
    ));

    context.register(placedSkyGeode, new PlacedFeature(
      configuredFeatureRegistry.getOrThrow(configuredSkyGeode),
      List.of(RarityFilter.onAverageOnceEvery(64), HeightRangePlacement.uniform(VerticalAnchor.absolute(16), VerticalAnchor.absolute(54)))
    ));

    context.register(placedIchorGeode, new PlacedFeature(
      configuredFeatureRegistry.getOrThrow(configuredIchorGeode),
      List.of(RarityFilter.onAverageOnceEvery(52), HeightRangePlacement.uniform(VerticalAnchor.belowTop(48), VerticalAnchor.belowTop(16)))
    ));

    context.register(placedEnderGeode, new PlacedFeature(
      configuredFeatureRegistry.getOrThrow(configuredEnderGeode),
      List.of(RarityFilter.onAverageOnceEvery(256), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(16), VerticalAnchor.aboveBottom(64)))
    ));
    context.register(placedSmallCobaltOre, new PlacedFeature(
      configuredFeatureRegistry.getOrThrow(configuredSmallCobaltOre),
      List.of(CountPlacement.of(5), InSquarePlacement.spread(), PlacementUtils.RANGE_8_8, BiomeFilter.biome())
    ));
    context.register(placedLargeCobaltOre, new PlacedFeature(
      configuredFeatureRegistry.getOrThrow(configuredLargeCobaltOre),
      List.of(CountPlacement.of(3), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(8), VerticalAnchor.absolute(32)), BiomeFilter.biome())
    ));
  }
  private static ResourceKey<PlacedFeature> key(String name) {
    return ResourceKey.create(Registries.PLACED_FEATURE, TConstruct.getResource(name));
  }
}
