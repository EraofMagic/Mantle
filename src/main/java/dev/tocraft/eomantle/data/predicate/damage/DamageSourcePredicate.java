package dev.tocraft.eomantle.data.predicate.damage;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import dev.tocraft.eomantle.data.loadable.Loadables;
import dev.tocraft.eomantle.data.predicate.IJsonPredicate;
import dev.tocraft.eomantle.data.predicate.PredicateRegistry;
import dev.tocraft.eomantle.data.predicate.TagPredicateRegistry;
import dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.IGenericLoader;

import java.util.List;
import java.util.function.Predicate;

import static dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.SingletonLoader.singleton;

/**
 * Predicate testing for damage sources
 */
public interface DamageSourcePredicate extends IJsonPredicate<DamageSource> {
  /** Predicate that matches all sources */
  DamageSourcePredicate ANY = simple(source -> true);
  /** Loader for item predicates */
  PredicateRegistry<DamageSource> LOADER = new TagPredicateRegistry<>("Damage Source Predicate", ANY, Loadables.DAMAGE_TYPE_TAG, (tag, source) -> source.is(tag));

  /** Damage that protection works against */
  DamageSourcePredicate CAN_PROTECT = simple(source -> !source.is(DamageTypeTags.BYPASSES_ENCHANTMENTS) && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY));

  @Override
  default IJsonPredicate<DamageSource> inverted() {
    return LOADER.invert(this);
  }

  /** Creates a simple predicate with no parameters */
  static DamageSourcePredicate simple(Predicate<DamageSource> predicate) {
    return singleton(loader -> new DamageSourcePredicate() {
      @Override
      public boolean matches(DamageSource source) {
        return predicate.test(source);
      }

      @Override
      public IGenericLoader<? extends DamageSourcePredicate> getLoader() {
        return loader;
      }
    });
  }


  /* Helper methods */

  /** Creates an and predicate */
  @SafeVarargs
  static IJsonPredicate<DamageSource> and(IJsonPredicate<DamageSource>... predicates) {
    return LOADER.and(List.of(predicates));
  }

  /** Creates an or predicate */
  @SafeVarargs
  static IJsonPredicate<DamageSource> or(IJsonPredicate<DamageSource>... predicates) {
    return LOADER.or(List.of(predicates));
  }
}
