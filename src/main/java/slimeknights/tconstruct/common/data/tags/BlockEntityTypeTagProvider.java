package slimeknights.tconstruct.common.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tables.TinkerTables;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class BlockEntityTypeTagProvider extends TagsProvider<BlockEntityType<?>> {
  @SuppressWarnings("deprecation")
  public BlockEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, Registries.BLOCK_ENTITY_TYPE, provider, TConstruct.MOD_ID, existingFileHelper);
  }

  @SuppressWarnings("unchecked")

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(TinkerTags.TileEntityTypes.CRAFTING_STATION_BLACKLIST)
      .add(
        Stream.of(BlockEntityType.FURNACE, BlockEntityType.BLAST_FURNACE, BlockEntityType.SMOKER, BlockEntityType.BREWING_STAND,
            TinkerTables.craftingStationTile.get(), TinkerTables.tinkerStationTile.get(), TinkerTables.partBuilderTile.get(),
            TinkerTables.partChestTile.get(), TinkerTables.tinkersChestTile.get(), TinkerTables.castChestTile.get(),
            TinkerSmeltery.basin.get(), TinkerSmeltery.table.get(),
            TinkerSmeltery.melter.get(), TinkerSmeltery.smeltery.get(), TinkerSmeltery.foundry.get()).map(BuiltInRegistries.BLOCK_ENTITY_TYPE::getResourceKey).map(x -> x.orElseThrow())
          .toArray(ResourceKey[]::new)
      );

  }

  @Override
  public String getName() {
    return "Tinkers' Construct Block Entity Type Tags";
  }
}
