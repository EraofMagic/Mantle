package dev.tocraft.eomantle.datagen;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.data.ExistingFileHelper;
import dev.tocraft.eomantle.Mantle;

import java.util.concurrent.CompletableFuture;

import static dev.tocraft.eomantle.datagen.MantleTags.Fluids.LAVA;
import static dev.tocraft.eomantle.datagen.MantleTags.Fluids.WATER;

/** Provider for tags added by mantle, generally not useful for other mods */
public class MantleFluidTagProvider extends FluidTagsProvider {
  public MantleFluidTagProvider(PackOutput output, CompletableFuture<Provider> holders, ExistingFileHelper existingFileHelper) {
    super(output, holders,  Mantle.modId, existingFileHelper);
  }

  @Override
  protected void addTags(Provider pProvider) {
    this.tag(WATER).add(Fluids.WATER, Fluids.FLOWING_WATER);
    this.tag(LAVA).add(Fluids.LAVA, Fluids.FLOWING_LAVA);
  }

  @Override
  public String getName() {
    return "Mantle Fluid Tag Provider";
  }
}
