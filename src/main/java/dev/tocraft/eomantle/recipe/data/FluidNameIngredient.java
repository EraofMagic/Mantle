package dev.tocraft.eomantle.recipe.data;

import lombok.RequiredArgsConstructor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import dev.tocraft.eomantle.data.loadable.Loadable;
import dev.tocraft.eomantle.data.loadable.Loadables;
import dev.tocraft.eomantle.data.loadable.primitive.IntLoadable;
import dev.tocraft.eomantle.data.loadable.record.RecordLoadable;
import dev.tocraft.eomantle.recipe.ingredient.FluidIngredient;

import java.util.List;

/** Datagen fluid ingredient to create an ingredient matching a fluid from another mod, should not be used outside datagen */
@RequiredArgsConstructor(staticName = "of")
public class FluidNameIngredient extends FluidIngredient {
  private static final RecordLoadable<FluidNameIngredient> LOADABLE = RecordLoadable.create(
    Loadables.RESOURCE_LOCATION.requiredField("fluid", i -> i.fluidName),
    IntLoadable.FROM_ONE.requiredField("amount", i -> i.amount),
    FluidNameIngredient::new);

  private final ResourceLocation fluidName;
  private final int amount;

  @Override
  public Loadable<?> loadable() {
    return LOADABLE;
  }

  @Override
  public boolean test(Fluid fluid) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getAmount(Fluid fluid) {
    return amount;
  }

  @Override
  protected List<FluidStack> getAllFluids() {
    throw new UnsupportedOperationException();
  }
}
