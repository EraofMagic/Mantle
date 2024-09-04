package dev.tocraft.eomantle.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.fluids.FluidType;
import dev.tocraft.eomantle.Mantle;
import dev.tocraft.eomantle.fluid.tooltip.AbstractFluidTooltipProvider;
import dev.tocraft.eomantle.fluid.tooltip.FluidTooltipHandler;

/** Mantle datagen for fluid tooltips. For mods, don't use this, use {@link AbstractFluidTooltipProvider} */
public class MantleFluidTooltipProvider extends AbstractFluidTooltipProvider {
  public MantleFluidTooltipProvider(PackOutput packOutput) {
    super(packOutput, Mantle.modId);
  }

  @Override
  protected void addFluids() {
    add("buckets")
      .addUnit("kilobucket", FluidType.BUCKET_VOLUME * 1000)
      .addUnit("bucket", FluidType.BUCKET_VOLUME);
    addRedirect(FluidTooltipHandler.DEFAULT_ID, id("buckets"));
  }

  @Override
  public String getName() {
    return "Mantle Fluid Tooltip Provider";
  }
}
