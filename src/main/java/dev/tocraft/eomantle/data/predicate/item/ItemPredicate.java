package dev.tocraft.eomantle.data.predicate.item;

import com.google.common.collect.ImmutableSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import dev.tocraft.eomantle.data.loadable.Loadables;
import dev.tocraft.eomantle.data.predicate.IJsonPredicate;
import dev.tocraft.eomantle.data.predicate.RegistryPredicateRegistry;
import dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.IGenericLoader;
import dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.SingletonLoader;
import dev.tocraft.eomantle.util.RegistryHelper;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/** Simple serializable item predicate */
public interface ItemPredicate extends IJsonPredicate<Item> {
  /** Predicate that matches all items */
  ItemPredicate ANY = simple(item -> true);
  /** Loader for item predicates */
  RegistryPredicateRegistry<Item,Item> LOADER = new RegistryPredicateRegistry<>("Item Predicate", ANY, Loadables.ITEM, Function.identity(), "items", Loadables.ITEM_TAG, RegistryHelper::contains);

  @Override
  default IJsonPredicate<Item> inverted() {
    return LOADER.invert(this);
  }

  /** Creates a new predicate singleton */
  static ItemPredicate simple(Predicate<Item> predicate) {
    return SingletonLoader.singleton(loader -> new ItemPredicate() {
      @Override
      public boolean matches(Item item) {
        return predicate.test(item);
      }

      @Override
      public IGenericLoader<? extends ItemPredicate> getLoader() {
        return loader;
      }
    });
  }


  /* Helper methods */

  /** Creates am item set predicate */
  static IJsonPredicate<Item> set(Item... items) {
    return LOADER.setOf(ImmutableSet.copyOf(items));
  }

  /** Creates a tag predicate */
  static IJsonPredicate<Item> tag(TagKey<Item> tag) {
    return LOADER.tag(tag);
  }

  /** Creates an and predicate */
  @SafeVarargs
  static IJsonPredicate<Item> and(IJsonPredicate<Item>... predicates) {
    return LOADER.and(List.of(predicates));
  }

  /** Creates an or predicate */
  @SafeVarargs
  static IJsonPredicate<Item> or(IJsonPredicate<Item>... predicates) {
    return LOADER.or(List.of(predicates));
  }
}
