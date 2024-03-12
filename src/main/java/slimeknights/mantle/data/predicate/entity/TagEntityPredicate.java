package slimeknights.mantle.data.predicate.entity;

import lombok.RequiredArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.mantle.data.loader.TagKeyLoader;
import slimeknights.mantle.data.registry.GenericLoaderRegistry.IGenericLoader;

/**
 * Predicate matching an entity tag
 */
@RequiredArgsConstructor
public class TagEntityPredicate implements LivingEntityPredicate {
  public static final TagKeyLoader<EntityType<?>,TagEntityPredicate> LOADER = new TagKeyLoader<>(Registry.ENTITY_TYPE_REGISTRY, TagEntityPredicate::new, c -> c.tag);

  private final TagKey<EntityType<?>> tag;

  @Override
  public boolean matches(LivingEntity entity) {
    return entity.getType().is(tag);
  }

  @Override
  public IGenericLoader<? extends LivingEntityPredicate> getLoader() {
    return LOADER;
  }
}