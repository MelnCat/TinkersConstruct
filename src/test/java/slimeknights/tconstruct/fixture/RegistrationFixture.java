package slimeknights.tconstruct.fixture;

import net.minecraft.resources.ResourceLocation;
import slimeknights.mantle.data.registry.GenericLoaderRegistry;
import slimeknights.mantle.data.registry.GenericLoaderRegistry.IGenericLoader;

/** Helpers for generic registration tasks */
public class RegistrationFixture {
  /** Registers an object to a registry without risk of tests failing if its registered already */
  public static <T> void register(GenericLoaderRegistry<? super T> registry, String name, IGenericLoader<T> value) {
    try {
      registry.register(new ResourceLocation("test", name), value);
    } catch (Exception e) {
      // no-op
    }
  }
}