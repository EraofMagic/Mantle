package dev.tocraft.eomantle.plugin.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import dev.tocraft.eomantle.Mantle;
import dev.tocraft.eomantle.client.screen.MultiModuleScreen;
import dev.tocraft.eomantle.inventory.MultiModuleContainerMenu;
import dev.tocraft.eomantle.plugin.jei.entity.EntityIngredientHelper;
import dev.tocraft.eomantle.plugin.jei.entity.EntityIngredientRenderer;
import dev.tocraft.eomantle.recipe.crafting.ShapedRetexturedRecipe;

import java.util.Collections;
import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return Mantle.getResource("jei");
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(MantleJEIConstants.ENTITY_TYPE, Collections.emptyList(), new EntityIngredientHelper(), new EntityIngredientRenderer(16));
  }

  @Override
  public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registry) {
    registry.getCraftingCategory().addCategoryExtension(ShapedRetexturedRecipe.class, RetexturableRecipeExtension::new);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addGuiContainerHandler(MultiModuleScreen.class, new MultiModuleContainerHandler());
  }

  private static class MultiModuleContainerHandler<C extends MultiModuleContainerMenu<?>> implements IGuiContainerHandler<MultiModuleScreen<C>> {
    @Override
    public List<Rect2i> getGuiExtraAreas(MultiModuleScreen<C> guiContainer) {
      return guiContainer.getModuleAreas();
    }
  }
}
