package slimeknights.tconstruct.tools.modifiers.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.traits.melee.LaceratingModifier;

import static slimeknights.tconstruct.common.structure.TCDamageTypes.bleedingDamage;

/**
 * Potion effect from {@link LaceratingModifier}
 */
public class BleedingEffect extends NoMilkEffect {
  public BleedingEffect() {
    super(MobEffectCategory.HARMFUL, 0xa80000, true);
  }

  @Override
  public boolean isDurationEffectTick(int tick, int level) {
    // every half second
    return tick > 0 && tick % 20 == 0;
  }

  @Override
  public void applyEffectTick(LivingEntity target, int level) {
    // attribute to player kill
    LivingEntity lastAttacker = target.getLastHurtMob();
    DamageSource source;
    if(lastAttacker != null) {
      source = new DamageSource(target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(bleedingDamage), null, lastAttacker);
    }
    else {
      source = new DamageSource(target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(bleedingDamage));
    }
    // perform damage
    int hurtResistantTime = target.invulnerableTime;
    ToolAttackUtil.attackEntitySecondary(source, (level + 1f) / 2f, target, target, true);
    target.invulnerableTime = hurtResistantTime;

    // damage particles
    if (target.level() instanceof ServerLevel) {
      ((ServerLevel)target.level()).sendParticles(ParticleTypes.DAMAGE_INDICATOR, target.getX(), target.getY(0.5), target.getZ(), 1, 0.1, 0, 0.1, 0.2);
    }
  }

}
