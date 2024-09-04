package dev.tocraft.eomantle.plugin.jei;

import mezz.jei.api.ingredients.IIngredientType;
import dev.tocraft.eomantle.recipe.ingredient.EntityIngredient.EntityInput;

public class MantleJEIConstants {
  /** Ingredient for an entity */
  public static final IIngredientType<EntityInput> ENTITY_TYPE = () -> EntityInput.class;
}
