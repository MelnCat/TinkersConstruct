package slimeknights.tconstruct.tools.modifiers.ability.armor.walker;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ArmorWalkModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap.Builder;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public abstract class AbstractWalkerModifier extends NoLevelsModifier implements ArmorWalkModifierHook {
  /** Gets the radius for this modifier */
  protected abstract float getRadius(IToolStackView tool, int level);

  /**
   * Called to modify a position
   * @param tool     Tool instance
   * @param level    Modifier level
   * @param living   Entity walking
   * @param world    World being walked in
   * @param target   Position target for effect
   * @param mutable  Mutable position you can freely modify
   */
  protected abstract void walkOn(IToolStackView tool, int level, LivingEntity living, Level world, BlockPos target, MutableBlockPos mutable);

  @Override
  public void onWalk(IToolStackView tool, ModifierEntry modifier, LivingEntity living, BlockPos prevPos, BlockPos newPos) {
    if (living.onGround() && !tool.isBroken() && !living.level().isClientSide) {
      float radius = Math.min(16, getRadius(tool, modifier.getLevel()));
      MutableBlockPos mutable = new MutableBlockPos();
      Level world = living.level();
      Vec3 posVec = living.position();
      BlockPos center = new BlockPos((int) posVec.x, (int) (posVec.y + 0.5), (int) posVec.z);
      for (BlockPos pos : BlockPos.betweenClosed(center.offset((int) -radius, 0, (int) -radius), center.offset((int) radius, 0, (int) radius))) {
        if (pos.closerToCenterThan(living.position(), radius)) {
          walkOn(tool, modifier.getLevel(), living, world, pos, mutable);
          if (tool.isBroken()) {
            break;
          }
        }
      }
    }
  }
  
  @Override
  protected void registerHooks(Builder hookBuilder) {
    super.registerHooks(hookBuilder);
    hookBuilder.addHook(this, ModifierHooks.BOOT_WALK);
  }
}
