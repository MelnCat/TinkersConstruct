package slimeknights.tconstruct.world.worldgen.trees;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import slimeknights.tconstruct.common.structure.ConfiguredFeatures;
import slimeknights.tconstruct.world.block.FoliageType;

public class SlimeTree extends AbstractTreeGrower {

  private final FoliageType foliageType;

  public SlimeTree(FoliageType foliageType) {
    this.foliageType = foliageType;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean largeHive) {
    return (ResourceKey<ConfiguredFeature<?, ?>>) (switch (this.foliageType) {
      case EARTH -> ConfiguredFeatures.earthSlimeTree;
      case SKY -> ConfiguredFeatures.skySlimeTree;
      case ENDER -> random.nextFloat() < 0.85f ? ConfiguredFeatures.enderSlimeTreeTall : ConfiguredFeatures.enderSlimeTree;
      case BLOOD -> ConfiguredFeatures.bloodSlimeFungus;
      case ICHOR -> ConfiguredFeatures.ichorSlimeFungus;
    });
  }
}
