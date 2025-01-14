package slimeknights.tconstruct.world.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureSet.StructureSelectionEntry;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride.BoundingBoxType;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.NotHolderSet;
import net.minecraftforge.registries.holdersets.OrHolderSet;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.structure.ConfiguredFeatures;
import slimeknights.tconstruct.common.structure.PlacedFeatures;
import slimeknights.tconstruct.common.structure.TCDamageTypes;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.FoliageType;
import slimeknights.tconstruct.world.worldgen.islands.IslandStructure;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static net.minecraft.core.HolderSet.direct;
import static slimeknights.tconstruct.TConstruct.getResource;
import static slimeknights.tconstruct.common.structure.ConfiguredFeatures.*;
import static slimeknights.tconstruct.world.TinkerStructures.*;

/**
 * Provider for all our worldgen datapack registry stuff
 */
@SuppressWarnings("SameParameterValue")
public class WorldgenDatapackRegistryProvider extends DatapackBuiltinEntriesProvider {

  public WorldgenDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, Set<String> modIds) {
    super(output, registries,
      new RegistrySetBuilder().add(
        Registries.STRUCTURE, WorldgenDatapackRegistryProvider::generateStructures
      ).add(
        Registries.STRUCTURE_SET, WorldgenDatapackRegistryProvider::generateStructureSets
      ).add(Registries.CONFIGURED_FEATURE, ConfiguredFeatures::generateConfiguredFeatures)
        .add(Registries.PLACED_FEATURE, PlacedFeatures::generatePlacedFeatures)
        .add(Registries.DAMAGE_TYPE, TCDamageTypes::generateDamageTypes)
      , modIds);
  }

  public static void generateStructures(BootstapContext<Structure> context) {
    var biomeRegistry = context.lookup(Registries.BIOME);
    var treeRegistry = context.lookup(Registries.CONFIGURED_FEATURE);


    // earthslime island
    context.register(earthSlimeIsland, IslandStructure.seaBuilder()
      .addDefaultTemplates(getResource("islands/earth/"))
      .addTree(treeRegistry.getOrThrow(earthSlimeIslandTree), 1)
      .addSlimyGrass(FoliageType.EARTH)
      .build(new StructureSettings(biomeRegistry.getOrThrow(TinkerTags.Biomes.EARTHSLIME_ISLANDS), monsterOverride(EntityType.SLIME, 4, 4), Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
    // skyslime island
    context.register(skySlimeIsland, IslandStructure.skyBuilder()
      .addDefaultTemplates(getResource("islands/sky/"))
      .addTree(treeRegistry.getOrThrow(skySlimeIslandTree), 1)
      .addSlimyGrass(FoliageType.SKY)
      .vines(TinkerWorld.skySlimeVine.get())
      .build(new StructureSettings(biomeRegistry.getOrThrow(TinkerTags.Biomes.SKYSLIME_ISLANDS), monsterOverride(TinkerWorld.skySlimeEntity.get(), 3, 4), Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
    // clay island
    context.register(clayIsland, IslandStructure.skyBuilder().addDefaultTemplates(getResource("islands/dirt/"))
      .addTree(treeRegistry.getOrThrow(TreeFeatures.OAK), 4)
      .addTree(treeRegistry.getOrThrow(TreeFeatures.BIRCH), 3)
      .addTree(treeRegistry.getOrThrow(TreeFeatures.SPRUCE), 2)
      .addTree(treeRegistry.getOrThrow(TreeFeatures.ACACIA), 1)
      .addTree(treeRegistry.getOrThrow(TreeFeatures.JUNGLE_TREE_NO_VINE), 1)
      .addGrass(Blocks.GRASS, 7)
      .addGrass(Blocks.FERN, 1)
      .build(new StructureSettings(biomeRegistry.getOrThrow(TinkerTags.Biomes.CLAY_ISLANDS), monsterOverride(TinkerWorld.terracubeEntity.get(), 2, 4), Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));
    // blood island
    context.register(bloodIsland, IslandStructure.seaBuilder().addDefaultTemplates(getResource("islands/blood/"))
      .addTree(treeRegistry.getOrThrow(bloodSlimeIslandFungus), 1)
      .addSlimyGrass(FoliageType.BLOOD)
      .build(new StructureSettings(biomeRegistry.getOrThrow(TinkerTags.Biomes.BLOOD_ISLANDS), monsterOverride(EntityType.MAGMA_CUBE, 4, 6), Decoration.UNDERGROUND_DECORATION, TerrainAdjustment.NONE)));
    // enderslime
    context.register(endSlimeIsland, IslandStructure.skyBuilder().addDefaultTemplates(getResource("islands/ender/"))
      .addTree(treeRegistry.getOrThrow(enderSlimeTree), 3)
      .addTree(treeRegistry.getOrThrow(enderSlimeTreeTall), 17)
      .addSlimyGrass(FoliageType.ENDER)
      .vines(TinkerWorld.enderSlimeVine.get())
      .build(new StructureSettings(biomeRegistry.getOrThrow(TinkerTags.Biomes.ENDERSLIME_ISLANDS), monsterOverride(TinkerWorld.enderSlimeEntity.get(), 4, 4), Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE)));

  }

  public static void generateStructureSets(BootstapContext<StructureSet> context) {
    var structureRegistry = context.lookup(Registries.STRUCTURE);
    context.register(ResourceKey.create(Registries.STRUCTURE_SET, TConstruct.getResource("overworld_ocean_island")),
      structureSet(new RandomSpreadStructurePlacement(35, 25, RandomSpreadType.LINEAR, 25988585), new StructureSelectionEntry(structureRegistry.getOrThrow(earthSlimeIsland), 1))
    );
    context.register(ResourceKey.create(Registries.STRUCTURE_SET, TConstruct.getResource("overworld_sky_island")),
      structureSet(new RandomSpreadStructurePlacement(40, 15, RandomSpreadType.LINEAR, 14357800),
        new StructureSelectionEntry(structureRegistry.getOrThrow(skySlimeIsland), 4),
        new StructureSelectionEntry(structureRegistry.getOrThrow(clayIsland), 1)
      )
    );
    context.register(ResourceKey.create(Registries.STRUCTURE_SET, TConstruct.getResource("nether_ocean_island")),
      structureSet(new RandomSpreadStructurePlacement(15, 10, RandomSpreadType.LINEAR, 65245622), new StructureSelectionEntry(structureRegistry.getOrThrow(bloodIsland), 1))
    );
    context.register(ResourceKey.create(Registries.STRUCTURE_SET, TConstruct.getResource("end_sky_island")),
      structureSet(new RandomSpreadStructurePlacement(25, 12, RandomSpreadType.LINEAR, 368963602), new StructureSelectionEntry(structureRegistry.getOrThrow(endSlimeIsland), 1))
    );
  }

  public static void generateBiomeModifiers(BootstapContext<BiomeModifier> context) {
    var biomeRegistry = context.lookup(Registries.BIOME);
    var placedFeatureRegistry = context.lookup(Registries.PLACED_FEATURE);

    var overworld = biomeRegistry.getOrThrow(BiomeTags.IS_OVERWORLD);
    var nether = biomeRegistry.getOrThrow(BiomeTags.IS_NETHER);
    var end = biomeRegistry.getOrThrow(BiomeTags.IS_END);

    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("cobalt_ore")),
      new AddFeaturesBiomeModifier(
        nether,
        direct(
          placedFeatureRegistry.getOrThrow(PlacedFeatures.placedSmallCobaltOre),
          placedFeatureRegistry.getOrThrow(PlacedFeatures.placedLargeCobaltOre)
        ),
        Decoration.UNDERGROUND_DECORATION
      )
    );
    // geodes

    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("earth_geode")),
      new AddFeaturesBiomeModifier(
        nether,
        direct(
          placedFeatureRegistry.getOrThrow(PlacedFeatures.placedEarthGeode)
        ),
        Decoration.LOCAL_MODIFICATIONS
      )
    );
    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("sky_geode")),
      new AddFeaturesBiomeModifier(
        nether,
        direct(
          placedFeatureRegistry.getOrThrow(PlacedFeatures.placedEarthGeode)
        ),
        Decoration.LOCAL_MODIFICATIONS
      )
    );
    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("ichor_geode")),
      new AddFeaturesBiomeModifier(new AndHolderSet<>(
        List.of(
          overworld,
          new NotHolderSet<>(context.registryLookup(Registries.BIOME).orElseThrow(),
            new OrHolderSet<>(
              List.of(
                biomeRegistry.getOrThrow(BiomeTags.IS_OCEAN),
                biomeRegistry.getOrThrow(BiomeTags.IS_DEEP_OCEAN),
                biomeRegistry.getOrThrow(BiomeTags.IS_BEACH),
                biomeRegistry.getOrThrow(BiomeTags.IS_RIVER)
              )
            )
          )
        )
      ), direct(placedFeatureRegistry.getOrThrow(PlacedFeatures.placedSkyGeode)), Decoration.LOCAL_MODIFICATIONS)
    );
    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("ender_geode")),
      new AddFeaturesBiomeModifier(
        new AndHolderSet<>(
          List.of(end,
            new NotHolderSet<>(
              context.registryLookup(Registries.BIOME).orElseThrow(),
              direct(biomeRegistry.getOrThrow(Biomes.THE_END))
            ))
        ),
        direct(placedFeatureRegistry.getOrThrow(PlacedFeatures.placedEnderGeode)),
        Decoration.LOCAL_MODIFICATIONS
      )
    );
    // spawns

    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("spawn_overworld_slime")),
      new AddSpawnsBiomeModifier(overworld, List.of(new SpawnerData(TinkerWorld.skySlimeEntity.get(), 100, 2, 4)))
    );
    context.register(
      ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, TConstruct.getResource("spawn_end_slime")),
      new AddSpawnsBiomeModifier(end, List.of(new SpawnerData(TinkerWorld.enderSlimeEntity.get(), 10, 2, 4)))
    );

  }

  @Override
  public String getName() {
    return "Tinkers' Construct Worldgen Datapack Registries";
  }


  /**
   * Saves a structure set
   */
  private static StructureSet structureSet(StructurePlacement placement, StructureSelectionEntry... structures) {
    return new StructureSet(List.of(structures), placement);
  }


  /**
   * Creates a spawn override for a single mob
   */
  private static Map<MobCategory, StructureSpawnOverride> monsterOverride(EntityType<?> entity, int min, int max) {
    return Map.of(MobCategory.MONSTER, new StructureSpawnOverride(BoundingBoxType.STRUCTURE, WeightedRandomList.create(new MobSpawnSettings.SpawnerData(entity, 1, min, max))));
  }
}
