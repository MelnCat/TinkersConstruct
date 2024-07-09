package slimeknights.tconstruct.library.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.server.ServerLifecycleHooks;
import slimeknights.mantle.data.loadable.primitive.StringLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.predicate.IJsonPredicate;
import slimeknights.mantle.data.predicate.damage.DamageSourcePredicate;
import slimeknights.mantle.data.predicate.damage.SourceMessagePredicate;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;

public record SourceTypePredicate(ResourceKey<DamageType> type) implements DamageSourcePredicate {
  public static final RecordLoadable<SourceTypePredicate> LOADER = RecordLoadable.create(
    StringLoadable.DEFAULT.requiredField("type", (SourceTypePredicate x) -> x.type.location().toString()), (String x) -> new SourceTypePredicate(
      ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(x))
    )
  );

  public boolean matches(DamageSource source) {
    return source.is(this.type);
  }

  public GenericLoaderRegistry.IGenericLoader<? extends DamageSourcePredicate> getLoader() {
    return LOADER;
  }
}
