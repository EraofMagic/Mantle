package dev.tocraft.eomantle.data.predicate.damage;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import dev.tocraft.eomantle.data.loadable.record.RecordLoadable;
import dev.tocraft.eomantle.data.predicate.IJsonPredicate;
import dev.tocraft.eomantle.data.predicate.entity.LivingEntityPredicate;
import dev.tocraft.eomantle.data.registry.GenericLoaderRegistry.IGenericLoader;

/**
 * Predicate that checks for properties of the attacker in a damage source
 */
public record SourceAttackerPredicate(IJsonPredicate<LivingEntity> attacker) implements DamageSourcePredicate {
  public static final RecordLoadable<SourceAttackerPredicate> LOADER = RecordLoadable.create(LivingEntityPredicate.LOADER.directField("entity_type", SourceAttackerPredicate::attacker), SourceAttackerPredicate::new);

  @Override
  public boolean matches(DamageSource source) {
    return source.getEntity() instanceof LivingEntity living && attacker.matches(living);
  }

  @Override
  public IGenericLoader<? extends DamageSourcePredicate> getLoader() {
    return LOADER;
  }
}
