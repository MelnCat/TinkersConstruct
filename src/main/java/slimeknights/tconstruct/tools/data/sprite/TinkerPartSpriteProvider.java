package slimeknights.tconstruct.tools.data.sprite;

import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.item.ArmorSlotType;
import slimeknights.tconstruct.tools.stats.PlatingMaterialStats;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

/**
 * This class handles all tool part sprites generated by Tinkers' Construct. You can freely use this in your addon to generate TiC part textures for a new material
 * Do not use both this and {@link TinkerMaterialSpriteProvider} in a single generator for an addon, if you need to use both make two instances of {@link slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator}
 */
public class TinkerPartSpriteProvider extends AbstractPartSpriteProvider {
  public static final MaterialStatsId WOOD = new MaterialStatsId(TConstruct.MOD_ID, "wood");
  public static final MaterialStatsId PLATE = new MaterialStatsId(TConstruct.MOD_ID, "plate");
  public static final MaterialStatsId SLIMESUIT = new MaterialStatsId(TConstruct.MOD_ID, "slimesuit");
  public static final MaterialStatsId ARMOR_PLATING = new MaterialStatsId(TConstruct.MOD_ID, "armor_plating");
  public static final MaterialStatsId ARMOR_CHAINMAIL = new MaterialStatsId(TConstruct.MOD_ID, "armor_chainmail");

  public TinkerPartSpriteProvider() {
    super(TConstruct.MOD_ID);
  }

  @Override
  public String getName() {
    return "Tinkers' Construct Parts";
  }

  @Override
  protected void addAllSpites() {
    // heads
    addHead("round_plate");
    addHead("large_plate");
    addHead("small_blade");
    // handles
    addHandle("tool_handle");
    addHandle("tough_handle");
    // bow
    addBowstring("bowstring");
    // misc
    addBinding("tool_binding");
    addPart("repair_kit", StatlessMaterialStats.REPAIR_KIT.getIdentifier());

    // plate textures
    addPart("chainmail", StatlessMaterialStats.CHAINMAIL.getIdentifier());
    for (ArmorSlotType slot : ArmorSlotType.values()) {
      buildTool("armor/plate/" + slot.getSerializedName())
        .addBreakablePart("plating", PlatingMaterialStats.TYPES.get(slot.getIndex()).getId())
        .addBreakablePart("chainmail", StatlessMaterialStats.CHAINMAIL.getIdentifier());
    }
    addTexture("models/armor/plate/armor_1", ARMOR_PLATING);
    addTexture("models/armor/plate/leggings_1", ARMOR_PLATING);
    addTexture("models/armor/plate/armor_2", ARMOR_CHAINMAIL);
    addTexture("models/armor/plate/leggings_2", ARMOR_CHAINMAIL);

    // shield textures
    addSprite("armor/travelers/shield_modifiers/tconstruct_embellishment", PLATE);
    addSprite("armor/travelers/shield_modifiers/broken/tconstruct_embellishment", PLATE);
    addSprite("armor/plate/shield_modifiers/tconstruct_embellishment", PLATE);
    addSprite("armor/plate/shield_modifiers/broken/tconstruct_embellishment", PLATE);
    addSprite("armor/plate/shield_large_modifiers/tconstruct_embellishment", PLATE);
    addSprite("armor/plate/shield_large_modifiers/broken/tconstruct_embellishment", PLATE);

    // staff
    addSprite("staff/modifiers/tconstruct_embellishment", WOOD);
    addSprite("staff/large_modifiers/tconstruct_embellishment", WOOD);

    // slimesuit textures
    addSprite("armor/slime/skull_modifiers/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/skull_modifiers/broken/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/wings_modifiers/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/wings_modifiers/broken/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/shell_modifiers/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/shell_modifiers/broken/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/boot_modifiers/tconstruct_embellishment", SLIMESUIT);
    addSprite("armor/slime/boot_modifiers/broken/tconstruct_embellishment", SLIMESUIT);
    addTexture("models/armor/slime/layer_1", SLIMESUIT);
    addTexture("models/armor/slime/layer_2", SLIMESUIT);
    addTexture("models/armor/slime/wings", SLIMESUIT);

    // tools
    // pickaxe
    buildTool("pickaxe").addBreakableHead("head").addHandle("handle").addBinding("binding");
    buildTool("sledge_hammer").withLarge().addBreakableHead("head").addBreakableHead("back").addBreakableHead("front").addHandle("handle");
    buildTool("vein_hammer").withLarge().addBreakableHead("head").addHead("back").addBreakableHead("front").addHandle("handle");
    // shovel
    buildTool("mattock").addBreakableHead("axe").addBreakableHead("pick"); // handle provided by pickaxe
    buildTool("pickadze").addBreakableHead("axe"); // handle and "pick" head provided by other tools
    buildTool("excavator").withLarge().addBreakableHead("head").addHead("binding").addHandle("handle").addHandle("grip");
    // axe
    buildTool("hand_axe").addBreakableHead("head").addBinding("binding"); // handle provided by pickaxe
    buildTool("broad_axe").withLarge().addBreakableHead("blade").addBreakableHead("back").addHandle("handle").addBinding("binding");
    // scythe
    buildTool("kama").addBreakableHead("head").addBinding("binding"); // handle provided by pickaxe
    buildTool("scythe").withLarge().addBreakableHead("head").addHandle("handle").addHandle("accessory").addBinding("binding");
    // sword
    buildTool("dagger").addBreakableHead("blade").addHandle("crossguard");
    buildTool("sword").addBreakableHead("blade").addHandle("guard").addHandle("handle");
    buildTool("cleaver").withLarge().addBreakableHead("head").addBreakableHead("shield").addHandle("handle").addHandle("guard");
    // bow
    buildTool("crossbow")
      .addLimb("limb").addGrip("body")
      .addBowstring("bowstring").addBowstring("bowstring_1").addBowstring("bowstring_2").addBowstring("bowstring_3");
    buildTool("longbow").withLarge()
      .addLimb("limb_bottom").addLimb("limb_bottom_1").addLimb("limb_bottom_2").addLimb("limb_bottom_3")
      .addLimb("limb_top").addLimb("limb_top_1").addLimb("limb_top_2").addLimb("limb_top_3")
      .addGrip("grip")
      .addBreakableBowstring("bowstring").addBowstring("bowstring_1").addBowstring("bowstring_2").addBowstring("bowstring_3");
  }
}
