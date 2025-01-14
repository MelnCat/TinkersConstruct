package slimeknights.tconstruct.tables;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.item.RetexturedBlockItem;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.TinkerModule;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.config.Config;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.ItemPartRecipe;
import slimeknights.tconstruct.library.recipe.partbuilder.PartRecipe;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipe;
import slimeknights.tconstruct.library.tools.layout.StationSlotLayoutLoader;
import slimeknights.tconstruct.library.utils.SimpleRecipeSerializer;
import slimeknights.tconstruct.shared.block.TableBlock;
import slimeknights.tconstruct.tables.block.*;
import slimeknights.tconstruct.tables.block.entity.chest.CastChestBlockEntity;
import slimeknights.tconstruct.tables.block.entity.chest.PartChestBlockEntity;
import slimeknights.tconstruct.tables.block.entity.chest.TinkersChestBlockEntity;
import slimeknights.tconstruct.tables.block.entity.table.CraftingStationBlockEntity;
import slimeknights.tconstruct.tables.block.entity.table.ModifierWorktableBlockEntity;
import slimeknights.tconstruct.tables.block.entity.table.PartBuilderBlockEntity;
import slimeknights.tconstruct.tables.block.entity.table.TinkerStationBlockEntity;
import slimeknights.tconstruct.tables.data.TableRecipeProvider;
import slimeknights.tconstruct.tables.item.TableBlockItem;
import slimeknights.tconstruct.tables.item.TinkersChestBlockItem;
import slimeknights.tconstruct.tables.menu.*;
import slimeknights.tconstruct.tables.recipe.*;

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Handles all the table for tool creation
 */
@SuppressWarnings("unused")
public final class TinkerTables extends TinkerModule {

  /*
   * Blocks
   */
  private static final Block.Properties WOOD_TABLE = builder(SoundType.WOOD).ignitedByLava().strength(1.0F, 5.0F).noOcclusion();
  /** Call with .apply to set the tag type for a block item provider */
  private static final BiFunction<TagKey<Item>,BooleanSupplier,Function<Block,RetexturedBlockItem>> RETEXTURED_BLOCK_ITEM = (tag, cond) -> block -> new TableBlockItem(block, tag, GENERAL_PROPS, cond);
  public static final ItemObject<TableBlock> craftingStation = BLOCKS.register("crafting_station", () -> new CraftingStationBlock(WOOD_TABLE), RETEXTURED_BLOCK_ITEM.apply(ItemTags.LOGS, Config.COMMON.showAllTableVariants::get));
  public static final ItemObject<TableBlock> tinkerStation = BLOCKS.register("tinker_station", () -> new TinkerStationBlock(WOOD_TABLE, 4), RETEXTURED_BLOCK_ITEM.apply(ItemTags.PLANKS, Config.COMMON.showAllTableVariants::get));
  public static final ItemObject<TableBlock> partBuilder = BLOCKS.register("part_builder", () -> new GenericTableBlock(WOOD_TABLE, PartBuilderBlockEntity::new), RETEXTURED_BLOCK_ITEM.apply(ItemTags.PLANKS, Config.COMMON.showAllTableVariants::get));
  public static final ItemObject<TableBlock> tinkersChest = BLOCKS.register("tinkers_chest", () -> new TinkersChestBlock(WOOD_TABLE, TinkersChestBlockEntity::new, true), block -> new TinkersChestBlockItem(block, GENERAL_PROPS));
  public static final ItemObject<TableBlock> partChest = BLOCKS.register("part_chest", () -> new ChestBlock(WOOD_TABLE, PartChestBlockEntity::new, true), GENERAL_BLOCK_ITEM);

  public static final ItemObject<TableBlock> modifierWorktable = BLOCKS.register("modifier_worktable",
    () -> new GenericTableBlock(builder(SoundType.STONE).requiresCorrectToolForDrops().strength(3.5F).noOcclusion(), ModifierWorktableBlockEntity::new),
    RETEXTURED_BLOCK_ITEM.apply(TinkerTags.Items.WORKSTATION_ROCK, Config.COMMON.showAllTableVariants::get));

  private static final Block.Properties METAL_TABLE = builder(SoundType.ANVIL).pushReaction(PushReaction.BLOCK).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).noOcclusion();
  public static final ItemObject<TableBlock> tinkersAnvil = BLOCKS.register("tinkers_anvil", () -> new TinkersAnvilBlock(METAL_TABLE, 6), RETEXTURED_BLOCK_ITEM.apply(TinkerTags.Items.ANVIL_METAL, Config.COMMON.showAllAnvilVariants::get));
  public static final ItemObject<TableBlock> scorchedAnvil = BLOCKS.register("scorched_anvil", () -> new ScorchedAnvilBlock(METAL_TABLE, 6), RETEXTURED_BLOCK_ITEM.apply(TinkerTags.Items.ANVIL_METAL, Config.COMMON.showAllAnvilVariants::get));
  private static final Block.Properties STONE_TABLE = builder(SoundType.METAL).requiresCorrectToolForDrops().strength(3.0F, 9.0F).noOcclusion();
  public static final ItemObject<TableBlock> castChest = BLOCKS.register("cast_chest", () -> new ChestBlock(STONE_TABLE, CastChestBlockEntity::new, false), GENERAL_BLOCK_ITEM);

  /*
   * Items
   */
  public static final ItemObject<Item> pattern = ITEMS.register("pattern", GENERAL_PROPS);

  /*
   * Tile entites
   */
  public static final RegistryObject<BlockEntityType<CraftingStationBlockEntity>> craftingStationTile = BLOCK_ENTITIES.register("crafting_station", CraftingStationBlockEntity::new, craftingStation);
  public static final RegistryObject<BlockEntityType<TinkerStationBlockEntity>> tinkerStationTile = BLOCK_ENTITIES.register("tinker_station", TinkerStationBlockEntity::new, builder ->
    builder.add(tinkerStation.get(), tinkersAnvil.get(), scorchedAnvil.get()));
  public static final RegistryObject<BlockEntityType<PartBuilderBlockEntity>> partBuilderTile = BLOCK_ENTITIES.register("part_builder", PartBuilderBlockEntity::new, partBuilder);
  public static final RegistryObject<BlockEntityType<ModifierWorktableBlockEntity>> modifierWorktableTile = BLOCK_ENTITIES.register("modifier_worktable", ModifierWorktableBlockEntity::new, modifierWorktable);
  // legacy name as tile entities cannot be remapped
  public static final RegistryObject<BlockEntityType<TinkersChestBlockEntity>> tinkersChestTile = BLOCK_ENTITIES.register("modifier_chest", TinkersChestBlockEntity::new, tinkersChest);
  public static final RegistryObject<BlockEntityType<PartChestBlockEntity>> partChestTile = BLOCK_ENTITIES.register("part_chest", PartChestBlockEntity::new, partChest);
  public static final RegistryObject<BlockEntityType<CastChestBlockEntity>> castChestTile = BLOCK_ENTITIES.register("cast_chest", CastChestBlockEntity::new, castChest);

  /*
   * Containers
   */
  public static final RegistryObject<MenuType<CraftingStationContainerMenu>> craftingStationContainer = MENUS.register("crafting_station", CraftingStationContainerMenu::new);
  public static final RegistryObject<MenuType<TinkerStationContainerMenu>> tinkerStationContainer = MENUS.register("tinker_station", TinkerStationContainerMenu::new);
  public static final RegistryObject<MenuType<PartBuilderContainerMenu>> partBuilderContainer = MENUS.register("part_builder", PartBuilderContainerMenu::new);
  public static final RegistryObject<MenuType<ModifierWorktableContainerMenu>> modifierWorktableContainer = MENUS.register("modifier_worktable", ModifierWorktableContainerMenu::new);
  public static final RegistryObject<MenuType<TinkerChestContainerMenu>> tinkerChestContainer = MENUS.register("tinker_chest", TinkerChestContainerMenu::new);

  /*
   * Recipes
   */
  public static final RegistryObject<RecipeSerializer<MaterialRecipe>> materialRecipeSerializer = RECIPE_SERIALIZERS.register("material", () -> LoadableRecipeSerializer.of(MaterialRecipe.LOADER));
  public static final RegistryObject<RecipeSerializer<ToolBuildingRecipe>> toolBuildingRecipeSerializer = RECIPE_SERIALIZERS.register("tool_building", () -> LoadableRecipeSerializer.of(ToolBuildingRecipe.LOADER));
  public static final RegistryObject<SimpleRecipeSerializer<TinkerStationPartSwapping>> tinkerStationPartSwappingSerializer = RECIPE_SERIALIZERS.register("tinker_station_part_swapping", () -> new SimpleRecipeSerializer<>(TinkerStationPartSwapping::new));
  public static final RegistryObject<RecipeSerializer<TinkerStationDamagingRecipe>> tinkerStationDamagingSerializer = RECIPE_SERIALIZERS.register("tinker_station_damaging", () -> LoadableRecipeSerializer.of(TinkerStationDamagingRecipe.LOADER));
  // part builder
  public static final RegistryObject<RecipeSerializer<PartRecipe>> partRecipeSerializer = RECIPE_SERIALIZERS.register("part_builder", () -> LoadableRecipeSerializer.of(PartRecipe.LOADER));
  public static final RegistryObject<RecipeSerializer<ItemPartRecipe>> itemPartBuilderSerializer = RECIPE_SERIALIZERS.register("item_part_builder", () -> LoadableRecipeSerializer.of(ItemPartRecipe.LOADER));
  public static final RegistryObject<RecipeSerializer<PartBuilderToolRecycle>> partBuilderToolRecycling = RECIPE_SERIALIZERS.register("part_builder_tool_recycling", () -> LoadableRecipeSerializer.of(PartBuilderToolRecycle.LOADER));
  // repair - standard
  public static final RegistryObject<SimpleRecipeSerializer<TinkerStationRepairRecipe>> tinkerStationRepairSerializer = RECIPE_SERIALIZERS.register("tinker_station_repair", () -> new SimpleRecipeSerializer<>(TinkerStationRepairRecipe::new));
  public static final RegistryObject<SimpleRecipeSerializer<CraftingTableRepairKitRecipe>> craftingTableRepairSerializer = RECIPE_SERIALIZERS.register("crafting_table_repair", () -> new SimpleRecipeSerializer<>(CraftingTableRepairKitRecipe::new));

  @SubscribeEvent
  void commonSetup(final FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      StationSlotLayoutLoader loader = StationSlotLayoutLoader.getInstance();
      loader.registerRequiredLayout(tinkerStation.getId());
      loader.registerRequiredLayout(tinkersAnvil.getId());
      loader.registerRequiredLayout(scorchedAnvil.getId());
    });
  }

  @SubscribeEvent
  void gatherData(final GatherDataEvent event) {
    DataGenerator datagenerator = event.getGenerator();
    datagenerator.addProvider(event.includeServer(), new TableRecipeProvider(datagenerator.getPackOutput()));
  }
}
