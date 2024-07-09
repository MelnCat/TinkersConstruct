package slimeknights.tconstruct.tables.item;

import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import slimeknights.mantle.item.RetexturedBlockItem;

import java.util.function.BooleanSupplier;

/** Retextured block that conditionally enables show all variants */
public class TableBlockItem extends RetexturedBlockItem {
  private final BooleanSupplier showAllCondition;
  public TableBlockItem(Block block, TagKey<Item> textureTag, Properties builder, BooleanSupplier showAllCondition) {
    super(block, textureTag, builder);
    this.showAllCondition = showAllCondition;
  }
}
