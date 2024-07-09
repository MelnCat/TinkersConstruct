package slimeknights.tconstruct.common.data.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.registration.object.EnumObject;
import slimeknights.mantle.registration.object.MetalItemObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.gadgets.TinkerGadgets;
import slimeknights.tconstruct.gadgets.entity.FrameType;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.smeltery.block.entity.module.EntityMeltingModule;
import slimeknights.tconstruct.tables.TinkerTables;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerToolParts;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.item.ArmorSlotType;
import slimeknights.tconstruct.world.TinkerHeadType;
import slimeknights.tconstruct.world.TinkerWorld;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.tags.ItemTags.CLUSTER_MAX_HARVESTABLES;
import static slimeknights.tconstruct.common.TinkerTags.Items.*;

@SuppressWarnings("unchecked")
public class DamageTypeTagProvider extends DamageTypeTagsProvider {


  public DamageTypeTagProvider(PackOutput p_270719_, CompletableFuture<HolderLookup.Provider> p_270256_, String modId, @Nullable ExistingFileHelper existingFileHelper) {
    super(p_270719_, p_270256_, modId, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    // Make this later configurable
    /*
      actions.add(new ConfigurableAction(builder, "wither", true, "Makes withering damage count as magic", DamageSource.WITHER::setMagic));
      actions.add(new ConfigurableAction(builder, "dragon_breath", true, "Makes dragons breath count as magic", DamageSource.DRAGON_BREATH::setMagic));
      /*actions.add(new ConfigurableAction(builder, "falling_block", false, "Makes falling blocks count as projectile", () -> {
        DamageSource.FALLING_BLOCK.setProjectile();
        DamageSource.ANVIL.setProjectile();
        DamageSource.FALLING_STALACTITE.setProjectile();
      }));
      actions.add(new ConfigurableAction(builder, "lightning", true, "Makes lightning count as fire damage", DamageSource.LIGHTNING_BOLT::setIsFire));*/
    // There is no more is_magic (except in Neoforge)
    tag(DamageTypeTags.IS_FIRE).add(DamageTypes.LIGHTNING_BOLT, EntityMeltingModule.SMELTERY_DAMAGE_TYPE.getKey());
  }

}
