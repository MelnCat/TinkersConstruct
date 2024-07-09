package slimeknights.tconstruct.common.structure;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.TConstruct;

public class TCDamageTypes {
  public static final ResourceKey<DamageType> bleedingDamage = key("bleeding");
  public static final ResourceKey<DamageType> selfDestructDamage = key("self_destruct");
  /** Standard damage source for melting most mobs */
  public static final ResourceKey<DamageType> smelteryDamage = key("smeltery_damage");
  /**
   * Special damage source for "absorbing" hot entities
   */

  public static final ResourceKey<DamageType> smelteryMagicDamage = key("smeltery_magic");


  public static void generateDamageTypes(BootstapContext<DamageType> context) {
    context.register(bleedingDamage, new DamageType(TConstruct.prefix("bleed"), 0));
    context.register(selfDestructDamage, new DamageType(TConstruct.prefix("self_destruct"), 0));
    context.register(smelteryDamage, new DamageType(TConstruct.prefix("smeltery_heat"), 0));
    context.register(smelteryMagicDamage, new DamageType(TConstruct.prefix("smeltery_magic"), 0));
  }
  private static ResourceKey<DamageType> key(String name) {
    return ResourceKey.create(Registries.DAMAGE_TYPE, TConstruct.getResource(name));
  }
}
