package slimeknights.tconstruct.common.structure;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MangrovePropaguleBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.RandomSpreadFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.UpwardsBranchingTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.injection.selectors.ITargetSelector;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.FoliageType;
import slimeknights.tconstruct.world.block.SlimeVineBlock;
import slimeknights.tconstruct.world.worldgen.islands.IslandStructure;
import slimeknights.tconstruct.world.worldgen.trees.ExtraRootVariantPlacer;
import slimeknights.tconstruct.world.worldgen.trees.LeaveVineDecorator;
import slimeknights.tconstruct.world.worldgen.trees.SupplierBlockStateProvider;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeFungusConfig;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeTreeConfig;

import java.util.Arrays;
import java.util.List;

import static slimeknights.tconstruct.TConstruct.getResource;
import static slimeknights.tconstruct.world.TinkerStructures.*;
import static slimeknights.tconstruct.world.TinkerStructures.slimeTree;
import static slimeknights.tconstruct.world.TinkerWorld.*;

public class ConfiguredFeatures {
  public static final ResourceKey<ConfiguredFeature<?, ?>> earthSlimeTree = key("earth_slime_tree");
  public static final ResourceKey<ConfiguredFeature<?, ?>> earthSlimeIslandTree = key("earth_slime_island_tree");
  public static final ResourceKey<ConfiguredFeature<?, ?>> skySlimeTree = key("sky_slime_tree");
  public static final ResourceKey<ConfiguredFeature<?, ?>> skySlimeIslandTree = key("sky_slime_island_tree");
  public static final ResourceKey<ConfiguredFeature<?, ?>> enderSlimeTree = key("ender_slime_tree");
  public static final ResourceKey<ConfiguredFeature<?, ?>> enderSlimeTreeTall = key("ender_slime_tree_tall");
  public static final ResourceKey<ConfiguredFeature<?, ?>> bloodSlimeFungus = key("blood_slime_fungus");
  public static final ResourceKey<ConfiguredFeature<?, ?>> bloodSlimeIslandFungus = key("blood_slime_island_fungus");
  public static final ResourceKey<ConfiguredFeature<?, ?>> ichorSlimeFungus = key("ichor_slime_fungus");

  public static final ResourceKey<ConfiguredFeature<?, ?>> configuredEarthGeode = key("earth_geode");
  public static final ResourceKey<ConfiguredFeature<?, ?>> configuredSkyGeode = key("sky_geode");
  public static final ResourceKey<ConfiguredFeature<?, ?>> configuredIchorGeode = key("ichor_geode");
  public static final ResourceKey<ConfiguredFeature<?, ?>> configuredEnderGeode = key("ender_geode");

  public static final ResourceKey<ConfiguredFeature<?, ?>> configuredSmallCobaltOre = key("cobalt_ore_small");
  public static final ResourceKey<ConfiguredFeature<?, ?>> configuredLargeCobaltOre = key("cobalt_ore_large");


  public static void generateConfiguredFeatures(BootstapContext<ConfiguredFeature<?, ?>> context) {
    /** Greenheart tree variant */
    context.register(earthSlimeTree, new ConfiguredFeature<>(slimeTree.get(), new SlimeTreeConfig.Builder()
      .planted()
      .trunk(() -> TinkerWorld.greenheart.getLog().defaultBlockState())
      .leaves(() -> TinkerWorld.slimeLeaves.get(FoliageType.EARTH).defaultBlockState())
      .baseHeight(4).randomHeight(3)
      .build())
    );
    /** Greenheart tree variant on islands */
    context.register(earthSlimeIslandTree, new ConfiguredFeature<>(slimeTree.get(),
      new SlimeTreeConfig.Builder()
        .trunk(() -> TinkerWorld.greenheart.getLog().defaultBlockState())
        .leaves(() -> TinkerWorld.slimeLeaves.get(FoliageType.EARTH).defaultBlockState())
        .baseHeight(4).randomHeight(3)
        .build())
    );
    /** Skyroot tree variant */
    context.register(skySlimeTree, new ConfiguredFeature<>(
      slimeTree.get(),
      new SlimeTreeConfig.Builder()
        .planted().canDoubleHeight()
        .trunk(() -> TinkerWorld.skyroot.getLog().defaultBlockState())
        .leaves(() -> TinkerWorld.slimeLeaves.get(FoliageType.SKY).defaultBlockState())
        .build())
    );
    /** Skyroot tree variant on islands */
    context.register(skySlimeIslandTree, new ConfiguredFeature<>(
      slimeTree.get(),
      new SlimeTreeConfig.Builder()
        .canDoubleHeight()
        .trunk(() -> TinkerWorld.skyroot.getLog().defaultBlockState())
        .leaves(() -> TinkerWorld.slimeLeaves.get(FoliageType.SKY).defaultBlockState())
        .vines(() -> TinkerWorld.skySlimeVine.get().defaultBlockState().setValue(SlimeVineBlock.STAGE, SlimeVineBlock.VineStage.MIDDLE))
        .build())
    );
    /** Enderslime short tree variant */
    context.register(enderSlimeTree, new ConfiguredFeature<>(
      Feature.TREE,
      new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(TinkerWorld.enderbark.getLog()),
        new UpwardsBranchingTrunkPlacer(2, 1, 4, UniformInt.of(1, 4), 0.5F, UniformInt.of(0, 1), BuiltInRegistries.BLOCK.getOrCreateTag(TinkerTags.Blocks.ENDERBARK_LOGS_CAN_GROW_THROUGH)),
        BlockStateProvider.simple(TinkerWorld.slimeLeaves.get(FoliageType.ENDER)),
        new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 70),
        ExtraRootVariantPlacer.builder()
          .trunkOffset(UniformInt.of(1, 3))
          .rootBlock(TinkerWorld.enderbarkRoots.get())
          .canGrowThroughTag(TinkerTags.Blocks.ENDERBARK_ROOTS_CAN_GROW_THROUGH)
          .slimyRoots(TinkerWorld.slimyEnderbarkRoots)
          .buildOptional(),
        new TwoLayersFeatureSize(2, 0, 2))
        .decorators(List.of(new LeaveVineDecorator(TinkerWorld.enderSlimeVine.get(), 0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, new RandomizedIntStateProvider(BlockStateProvider.simple(TinkerWorld.slimeSapling.get(FoliageType.ENDER).defaultBlockState().setValue(BlockStateProperties.HANGING, true)), MangrovePropaguleBlock.AGE, UniformInt.of(0, 4)), 2, List.of(Direction.DOWN))))
        .ignoreVines()
        .build())
    );
    /** Enderslime tall tree variant */
    context.register(enderSlimeTreeTall, new ConfiguredFeature<>(
      Feature.TREE,
      new TreeConfiguration.TreeConfigurationBuilder(BlockStateProvider.simple(TinkerWorld.enderbark.getLog()),
        new UpwardsBranchingTrunkPlacer(4, 1, 9, UniformInt.of(1, 6), 0.5F, UniformInt.of(0, 1), BuiltInRegistries.BLOCK.getOrCreateTag(TinkerTags.Blocks.ENDERBARK_LOGS_CAN_GROW_THROUGH)),
        BlockStateProvider.simple(TinkerWorld.slimeLeaves.get(FoliageType.ENDER)),
        new RandomSpreadFoliagePlacer(ConstantInt.of(3), ConstantInt.of(0), ConstantInt.of(2), 70),
        ExtraRootVariantPlacer.builder()
          .trunkOffset(UniformInt.of(3, 7))
          .rootBlock(TinkerWorld.enderbarkRoots.get())
          .canGrowThroughTag(TinkerTags.Blocks.ENDERBARK_ROOTS_CAN_GROW_THROUGH)
          .slimyRoots(TinkerWorld.slimyEnderbarkRoots)
          .buildOptional(),
        new TwoLayersFeatureSize(3, 0, 2))
        .decorators(List.of(new LeaveVineDecorator(TinkerWorld.enderSlimeVine.get(), 0.125F), new AttachedToLeavesDecorator(0.14F, 1, 0, new RandomizedIntStateProvider(BlockStateProvider.simple(TinkerWorld.slimeSapling.get(FoliageType.ENDER).defaultBlockState().setValue(BlockStateProperties.HANGING, true)), MangrovePropaguleBlock.AGE, UniformInt.of(0, 4)), 2, List.of(Direction.DOWN))))
        .ignoreVines()
        .build())
    );
    /** Bloodshroom tree variant */
    context.register(bloodSlimeFungus, new ConfiguredFeature<>(
      slimeFungus.get(),
      new SlimeFungusConfig(
        TinkerTags.Blocks.SLIMY_SOIL,
        TinkerWorld.bloodshroom.getLog().defaultBlockState(),
        TinkerWorld.slimeLeaves.get(FoliageType.BLOOD).defaultBlockState(),
        TinkerWorld.congealedSlime.get(SlimeType.ICHOR).defaultBlockState(),
        BlockPredicate.matchesTag(BlockTags.REPLACEABLE_BY_TREES),
        true)
    ));
    /** Bloodshroom island tree variant */
    context.register(bloodSlimeIslandFungus, new ConfiguredFeature<>(
      slimeFungus.get(),
      new SlimeFungusConfig(
        TinkerTags.Blocks.SLIMY_NYLIUM,
        TinkerWorld.bloodshroom.getLog().defaultBlockState(),
        TinkerWorld.slimeLeaves.get(FoliageType.BLOOD).defaultBlockState(),
        TinkerWorld.congealedSlime.get(SlimeType.ICHOR).defaultBlockState(),
        BlockPredicate.matchesTag(BlockTags.REPLACEABLE_BY_TREES),
        false)
    ));
    /** Deprecated ichor tree */
    context.register(ichorSlimeFungus, new ConfiguredFeature<>(
      slimeFungus.get(),
      new SlimeFungusConfig(
        TinkerTags.Blocks.SLIMY_SOIL,
        TinkerWorld.bloodshroom.getLog().defaultBlockState(),
        TinkerWorld.slimeLeaves.get(FoliageType.ICHOR).defaultBlockState(),
        TinkerWorld.congealedSlime.get(SlimeType.ICHOR).defaultBlockState(),
        BlockPredicate.matchesTag(BlockTags.REPLACEABLE_BY_TREES),
        false)
    ));
    registerGeode(
      context, configuredEarthGeode, earthGeode, BlockStateProvider.simple(Blocks.CALCITE), BlockStateProvider.simple(Blocks.CLAY),
      new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 5.2D), new GeodeCrackSettings(0.95D, 2.0D, 2), UniformInt.of(6, 9), UniformInt.of(3, 4), UniformInt.of(1, 2), 16, 1);

    registerGeode(
      context, configuredSkyGeode, skyGeode, BlockStateProvider.simple(Blocks.CALCITE), BlockStateProvider.simple(Blocks.MOSSY_COBBLESTONE),
      new GeodeLayerSettings(1.5D, 2.0D, 3.0D, 4.5D), new GeodeCrackSettings(0.55D, 0.5D, 2), UniformInt.of(3, 4), ConstantInt.of(2), ConstantInt.of(1), 8, 3
    );

    registerGeode(
      context, configuredIchorGeode, ichorGeode, BlockStateProvider.simple(Blocks.CALCITE), BlockStateProvider.simple(Blocks.NETHERRACK),
      new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 4.2D), new GeodeCrackSettings(0.75D, 2.0D, 2), UniformInt.of(4, 6), UniformInt.of(3, 4), UniformInt.of(1, 2), 24, 20
    );

    registerGeode(
      context, configuredEnderGeode, enderGeode, BlockStateProvider.simple(Blocks.CALCITE), BlockStateProvider.simple(Blocks.END_STONE),
      new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 5.2D), new GeodeCrackSettings(0.45D, 1.0D, 2), UniformInt.of(4, 10), UniformInt.of(3, 4), UniformInt.of(1, 2), 16, 10000
    );
    // small veins, standard distribution

    context.register(configuredSmallCobaltOre, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new BlockMatchTest(Blocks.NETHERRACK), cobaltOre.get().defaultBlockState(), 4)));
    // large veins, around y=16, up to 48
    context.register(configuredLargeCobaltOre, new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(new BlockMatchTest(Blocks.NETHERRACK), cobaltOre.get().defaultBlockState(), 6)));


  }

  private static void registerGeode(BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, GeodeItemObject geode,
                                    BlockStateProvider middleLayer, BlockStateProvider outerLayer, GeodeLayerSettings layerSettings, GeodeCrackSettings crackSettings,
                                    IntProvider outerWall, IntProvider distributionPoints, IntProvider pointOffset, int genOffset, int invalidBlocks) {
    context.register(key,
      new ConfiguredFeature<>(
        Feature.GEODE,
        new GeodeConfiguration(
          new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR),
            BlockStateProvider.simple(geode.getBlock()),
            SupplierBlockStateProvider.ofBlock(geode::getBudding),
            middleLayer, outerLayer,
            Arrays.stream(GeodeItemObject.BudSize.values()).map(type -> geode.getBud(type).defaultBlockState()).toList(),
            BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
          layerSettings, crackSettings, 0.335, 0.083, true, outerWall, distributionPoints, pointOffset, -genOffset, genOffset, 0.05D, invalidBlocks)
      ));
  }

  private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
    return ResourceKey.create(Registries.CONFIGURED_FEATURE, TConstruct.getResource(name));
  }
}
