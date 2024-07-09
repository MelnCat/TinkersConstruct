package slimeknights.tconstruct.library.modifiers.fluid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import static slimeknights.tconstruct.library.tools.helper.ModifierUtil.asLiving;
import static slimeknights.tconstruct.library.tools.helper.ModifierUtil.asPlayer;

/** Context for calling fluid effects */
@Getter
@RequiredArgsConstructor
public abstract class FluidEffectContext {
  protected final Level level;
  /** Entity using the fluid */
  @Nullable
  protected final LivingEntity entity;
  /** Player using the fluid, may be null if a non-player is the source of the fluid */
  @Nullable
  protected final Player player;
  /** Projectile that caused the fluid, null if no projectile is used (e.g. melee or interact effects) */
  @Nullable
  protected final Projectile projectile;

  /** Gets a damage source based on this context */
  public ModifiableDamageSource createDamageSource() {
    if (projectile != null) {
      return new ModifiableDamageSource(DamageTypes.MOB_PROJECTILE, null, projectile);
    }
    if (player != null) {
      return new ModifiableDamageSource(DamageTypes.PLAYER_ATTACK, null, player);
    }
    if (entity != null) {
      return new ModifiableDamageSource(DamageTypes.MOB_ATTACK, null, entity);
    }
    // we should never reach here, but just in case
    return new ModifiableDamageSource(DamageTypes.GENERIC, null, null);
  }

  /** Context for fluid effects targeting an entity */
  @Getter
  public static class Entity extends FluidEffectContext {
    private final net.minecraft.world.entity.Entity target;
    @Nullable
    private final LivingEntity livingTarget;
    public Entity(Level level, @Nullable LivingEntity holder, @Nullable Player player, @Nullable Projectile projectile, net.minecraft.world.entity.Entity target, @Nullable LivingEntity livingTarget) {
      super(level, holder, player, projectile);
      this.target = target;
      this.livingTarget = livingTarget;
    }

    public Entity(Level level, @Nullable LivingEntity holder, @Nullable Projectile projectile, net.minecraft.world.entity.Entity target) {
      this(level, holder, asPlayer(holder), projectile, target, asLiving(target));
    }

    public Entity(Level level, Player player, @Nullable Projectile projectile, LivingEntity target) {
      this(level, player, player, projectile, target, target);
    }
  }

  /** Context for fluid effects targeting an entity */
  public static class Block extends FluidEffectContext {
    @Getter
    private final BlockHitResult hitResult;
    private BlockState state;
    public Block(Level level, @Nullable LivingEntity holder, @Nullable Player player, @Nullable Projectile projectile, BlockHitResult hitResult) {
      super(level, holder, player, projectile);
      this.hitResult = hitResult;
    }

    public Block(Level level, @Nullable LivingEntity holder, @Nullable Projectile projectile, BlockHitResult hitResult) {
      this(level, holder, asPlayer(holder), projectile, hitResult);
    }

    public Block(Level level, @Nullable Player player, @Nullable Projectile projectile, BlockHitResult hitResult) {
      this(level, player, player, projectile, hitResult);
    }

    /** Gets the block state targeted by this context */
    public BlockState getBlockState() {
      if (state == null) {
        state = level.getBlockState(hitResult.getBlockPos());
      }
      return state;
    }

    /** Checks if the block in front of the hit block is replaceable */
    public boolean isOffsetReplaceable() {
      return level.getBlockState(hitResult.getBlockPos().relative(hitResult.getDirection())).canBeReplaced();
    }
  }

  public static class ModifiableDamageSource extends DamageSource {
    private Set<TagKey<DamageType>> tags = new HashSet<>();

    public ModifiableDamageSource(ResourceKey<DamageType> pType, @org.jetbrains.annotations.Nullable net.minecraft.world.entity.Entity pDirectEntity, @org.jetbrains.annotations.Nullable net.minecraft.world.entity.Entity pCausingEntity) {
      super(ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(pType), pDirectEntity, pCausingEntity);
    }

    public void addTag(TagKey<DamageType> tag) {
      tags.add(tag);
    }

    @Override
    public boolean is(TagKey<DamageType> pDamageTypeKey) {
      return tags.contains(pDamageTypeKey) || super.is(pDamageTypeKey);
    }
  }
}
