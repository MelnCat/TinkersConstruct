package slimeknights.tconstruct.library.modifiers.fluid.entity;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import slimeknights.mantle.data.loadable.primitive.FloatLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.NamedComponentRegistry;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.fluid.EffectLevel;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffect;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectContext.Entity;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;

import java.util.List;
import java.util.function.Consumer;

/**
 * Effect that damages an entity
 * @param modifiers  Additional properties to set on the damage source
 * @param damage     Amount of damage to apply
 */
public record DamageFluidEffect(List<TagKey<DamageType>> modifiers, float damage) implements FluidEffect<FluidEffectContext.Entity> {
  /** Registry of various damage sources */
  public static final NamedComponentRegistry<TagKey<DamageType>> SOURCE_MODIFIERS = new NamedComponentRegistry<>("Unregistered damage source modifier");
  /** Loader for this effect */
  public static final RecordLoadable<DamageFluidEffect> LOADER = RecordLoadable.create(
    SOURCE_MODIFIERS.list(0).defaultField("modifier", List.of(), e -> e.modifiers),
    FloatLoadable.FROM_ZERO.requiredField("damage", e -> e.damage),
    DamageFluidEffect::new);

  @SafeVarargs
  public DamageFluidEffect(float damage, TagKey<DamageType> ... modifiers) {
    this(List.of(modifiers), damage);
  }

  @Override
  public RecordLoadable<DamageFluidEffect> getLoader() {
    return LOADER;
  }

  @Override
  public float apply(FluidStack fluid, EffectLevel level, Entity context, FluidAction action) {
    float value = level.value();
    if (action.simulate()) {
      return value;
    }
    var source = context.createDamageSource();
    for (TagKey<DamageType> modifier : modifiers) {
      source.addTag(modifier);
    }
    return ToolAttackUtil.attackEntitySecondary(source, this.damage * value, context.getTarget(), context.getLivingTarget(), true) ? value : 0;
  }


  /** Makes the source fire damage */
  public static final TagKey<DamageType> FIRE = modifier("fire", DamageTypeTags.IS_FIRE);
  /** Makes the source explosion damage */
  public static final TagKey<DamageType> EXPLOSION = modifier("explosion", DamageTypeTags.IS_EXPLOSION);
  /** Makes the source magic damage */
  public static final TagKey<DamageType> MAGIC = modifier("magic", DamageTypeTags.BYPASSES_RESISTANCE); // Closest thing to magic
  /** Makes the source fall damage */
  public static final TagKey<DamageType> FALL = modifier("fall", DamageTypeTags.IS_FALL);
  /** Makes the source not make the target hostile */
  public static final TagKey<DamageType> NO_AGGRO = modifier("no_aggro", DamageTypeTags.NO_ANGER);
  /** Makes the damage bypass basic armor protection */
  public static final TagKey<DamageType> BYPASS_ARMOR = modifier("bypass_armor", DamageTypeTags.BYPASSES_ARMOR);
  /** Makes the damage bypass enchantments like protection */
  public static final TagKey<DamageType> BYPASS_ENCHANTMENTS = modifier("bypass_enchantments", DamageTypeTags.BYPASSES_ENCHANTMENTS);
  /** Makes the damage bypass potion effects and enchantments */
  public static final TagKey<DamageType> BYPASS_MAGIC = modifier("bypass_magic", DamageTypeTags.BYPASSES_EFFECTS);

  /** Registers a modifier locally */
  private static TagKey<DamageType> modifier(String name, TagKey<DamageType> tag) {
    SOURCE_MODIFIERS.register(TConstruct.getResource(name), tag);
    return tag;
  }
}
