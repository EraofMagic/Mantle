package dev.tocraft.eomantle.data.predicate;

import dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.IGenericLoader;
import dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.IHaveLoader;

/** Generic interface for predicate based JSON loaders */
public interface IJsonPredicate<I> extends IHaveLoader {
  /** Returns true if this json predicate matches the given input */
  boolean matches(I input);

  /** Inverts the given predicate */
  IJsonPredicate<I> inverted();

  @Override
  IGenericLoader<? extends IJsonPredicate<I>> getLoader();
}
