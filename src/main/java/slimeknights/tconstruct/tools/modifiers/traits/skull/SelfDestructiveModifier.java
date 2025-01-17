package slimeknights.tconstruct.tools.modifiers.traits.skull;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.KeybindInteractModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

import static slimeknights.tconstruct.common.structure.TCDamageTypes.bleedingDamage;
import static slimeknights.tconstruct.common.structure.TCDamageTypes.selfDestructDamage;

public class SelfDestructiveModifier extends NoLevelsModifier implements KeybindInteractModifierHook, EquipmentChangeModifierHook {
  /** Self damage source */

  @Override
  protected void registerHooks(Builder hookBuilder) {
    super.registerHooks(hookBuilder);
    hookBuilder.addHook(this, ModifierHooks.ARMOR_INTERACT, ModifierHooks.EQUIPMENT_CHANGE);
  }

  @Override
  public boolean startInteract(IToolStackView tool, ModifierEntry modifier, Player player, EquipmentSlot slot, TooltipKey keyModifier) {
    if (player.isShiftKeyDown()) {
      TinkerModifiers.selfDestructiveEffect.get().apply(player, 30, 2, true);
      player.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
      return true;
    }
    return false;
  }

  @Override
  public void stopInteract(IToolStackView tool, ModifierEntry modifier, Player player, EquipmentSlot slot) {
    player.removeEffect(TinkerModifiers.selfDestructiveEffect.get());
  }

  @Override
  public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
    context.getEntity().removeEffect(TinkerModifiers.selfDestructiveEffect.get());
  }

  /** Internal potion effect handling the explosion */
  public static class SelfDestructiveEffect extends NoMilkEffect {
    public SelfDestructiveEffect() {
      super(MobEffectCategory.HARMFUL, 0x59D24A, true);
      // make the player slow
      addAttributeModifier(Attributes.MOVEMENT_SPEED, "68ee3026-1d50-4eb4-914e-a8b05fbfdb71", -0.9f, Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
      return duration == 1;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
      // effect level is the explosion radius
      if (!living.level().isClientSide) {
        living.level().explode(living, living.getX(), living.getY(), living.getZ(), amplifier + 1, Level.ExplosionInteraction.MOB);
        living.hurt(new DamageSource(living.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(selfDestructDamage)), 99999);
      }
    }
  }
}
