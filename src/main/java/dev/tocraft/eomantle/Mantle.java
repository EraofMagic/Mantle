package dev.tocraft.eomantle;

import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dev.tocraft.eomantle.block.entity.MantleSignBlockEntity;
import dev.tocraft.eomantle.client.ClientEvents;
import dev.tocraft.eomantle.command.MantleCommand;
import dev.tocraft.eomantle.config.Config;
import dev.tocraft.eomantle.data.predicate.block.BlockPredicate;
import dev.tocraft.eomantle.data.predicate.block.BlockPropertiesPredicate;
import dev.tocraft.eomantle.data.predicate.damage.DamageSourcePredicate;
import dev.tocraft.eomantle.data.predicate.damage.SourceAttackerPredicate;
import dev.tocraft.eomantle.data.predicate.damage.SourceMessagePredicate;
import dev.tocraft.eomantle.data.predicate.entity.HasEnchantmentEntityPredicate;
import dev.tocraft.eomantle.data.predicate.entity.LivingEntityPredicate;
import dev.tocraft.eomantle.data.predicate.entity.MobTypePredicate;
import dev.tocraft.eomantle.data.predicate.item.ItemPredicate;
import dev.tocraft.eomantle.datagen.MantleFluidTagProvider;
import dev.tocraft.eomantle.datagen.MantleFluidTooltipProvider;
import dev.tocraft.eomantle.datagen.MantleTags;
import dev.tocraft.eomantle.fluid.transfer.EmptyFluidContainerTransfer;
import dev.tocraft.eomantle.fluid.transfer.EmptyFluidWithNBTTransfer;
import dev.tocraft.eomantle.fluid.transfer.FillFluidContainerTransfer;
import dev.tocraft.eomantle.fluid.transfer.FillFluidWithNBTTransfer;
import dev.tocraft.eomantle.fluid.transfer.FluidContainerTransferManager;
import dev.tocraft.eomantle.item.LecternBookItem;
import dev.tocraft.eomantle.loot.MantleLoot;
import dev.tocraft.eomantle.network.MantleNetwork;
import dev.tocraft.eomantle.recipe.crafting.ShapedFallbackRecipe;
import dev.tocraft.eomantle.recipe.crafting.ShapedRetexturedRecipe;
import dev.tocraft.eomantle.recipe.helper.TagEmptyCondition;
import dev.tocraft.eomantle.recipe.helper.TagPreference;
import dev.tocraft.eomantle.recipe.ingredient.FluidContainerIngredient;
import dev.tocraft.eomantle.registration.adapter.BlockEntityTypeRegistryAdapter;
import dev.tocraft.eomantle.registration.adapter.RegistryAdapter;
import dev.tocraft.eomantle.util.OffhandCooldownTracker;

import java.util.Objects;

/**
 * EoMantle
 *
 * Central mod object for Mantle
 *
 * @author Sunstrike <sun@sunstrike.io>
 */
@Mod(Mantle.modId)
public class Mantle {
  public static final String modId = "eomantle";
  public static final Logger logger = LogManager.getLogger("EoMantle");

  /* Instance of this mod, used for grabbing prototype fields */
  public static Mantle instance;

  /* Proxies for sides, used for graphics processing */
  public Mantle() {
    ModLoadingContext.get().registerConfig(Type.CLIENT, Config.CLIENT_SPEC);
    ModLoadingContext.get().registerConfig(Type.SERVER, Config.SERVER_SPEC);

    FluidContainerTransferManager.INSTANCE.init();
    MantleTags.init();

    instance = this;
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, this::commonSetup);
    bus.addListener(EventPriority.NORMAL, false, RegisterCapabilitiesEvent.class, this::registerCapabilities);
    bus.addListener(EventPriority.NORMAL, false, GatherDataEvent.class, this::gatherData);
    bus.addListener(EventPriority.NORMAL, false, RegisterEvent.class, this::register);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerInteractEvent.RightClickBlock.class, LecternBookItem::interactWithBlock);

    if (FMLEnvironment.dist == Dist.CLIENT) {
      ClientEvents.onConstruct();
    }
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    OffhandCooldownTracker.register(event);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {
    MantleNetwork.registerPackets();
    MantleCommand.init();
    OffhandCooldownTracker.init();
    TagPreference.init();
  }

  private void register(RegisterEvent event) {
    ResourceKey<?> key = event.getRegistryKey();
    if (key == Registries.RECIPE_SERIALIZER) {
      RegistryAdapter<RecipeSerializer<?>> adapter = new RegistryAdapter<>(Objects.requireNonNull(event.getForgeRegistry()));
      adapter.register(new ShapedFallbackRecipe.Serializer(), "crafting_shaped_fallback");
      adapter.register(new ShapedRetexturedRecipe.Serializer(), "crafting_shaped_retextured");

      CraftingHelper.register(TagEmptyCondition.SERIALIZER);
      CraftingHelper.register(FluidContainerIngredient.ID, FluidContainerIngredient.SERIALIZER);

      // fluid container transfer
      FluidContainerTransferManager.TRANSFER_LOADERS.registerDeserializer(EmptyFluidContainerTransfer.ID, EmptyFluidContainerTransfer.DESERIALIZER);
      FluidContainerTransferManager.TRANSFER_LOADERS.registerDeserializer(FillFluidContainerTransfer.ID, FillFluidContainerTransfer.DESERIALIZER);
      FluidContainerTransferManager.TRANSFER_LOADERS.registerDeserializer(EmptyFluidWithNBTTransfer.ID, EmptyFluidWithNBTTransfer.DESERIALIZER);
      FluidContainerTransferManager.TRANSFER_LOADERS.registerDeserializer(FillFluidWithNBTTransfer.ID, FillFluidWithNBTTransfer.DESERIALIZER);

      // predicates
      {
        // block predicates
        BlockPredicate.LOADER.register(getResource("requires_tool"), BlockPredicate.REQUIRES_TOOL.getLoader());
        BlockPredicate.LOADER.register(getResource("block_properties"), BlockPropertiesPredicate.LOADER);

        // item predicates
        //  make sure the item predicate registry is loaded, nothing to register here
        ItemPredicate.ANY.getLoader();

        // entity predicates
        // simple
        LivingEntityPredicate.LOADER.register(getResource("fire_immune"), LivingEntityPredicate.FIRE_IMMUNE.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("water_sensitive"), LivingEntityPredicate.WATER_SENSITIVE.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("on_fire"), LivingEntityPredicate.ON_FIRE.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("on_ground"), LivingEntityPredicate.ON_GROUND.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("crouching"), LivingEntityPredicate.CROUCHING.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("eyes_in_water"), LivingEntityPredicate.EYES_IN_WATER.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("feet_in_water"), LivingEntityPredicate.FEET_IN_WATER.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("underwater"), LivingEntityPredicate.UNDERWATER.getLoader());
        LivingEntityPredicate.LOADER.register(getResource("raining_at"), LivingEntityPredicate.RAINING.getLoader());
        // property
        LivingEntityPredicate.LOADER.register(getResource("mob_type"), MobTypePredicate.LOADER);
        LivingEntityPredicate.LOADER.register(getResource("has_enchantment"), HasEnchantmentEntityPredicate.LOADER);
        // register mob types
        MobTypePredicate.MOB_TYPES.register(new ResourceLocation("undefined"), MobType.UNDEFINED);
        MobTypePredicate.MOB_TYPES.register(new ResourceLocation("undead"), MobType.UNDEAD);
        MobTypePredicate.MOB_TYPES.register(new ResourceLocation("arthropod"), MobType.ARTHROPOD);
        MobTypePredicate.MOB_TYPES.register(new ResourceLocation("illager"), MobType.ILLAGER);
        MobTypePredicate.MOB_TYPES.register(new ResourceLocation("water"), MobType.WATER);

        // damage predicates
        // custom
        DamageSourcePredicate.LOADER.register(getResource("can_protect"), DamageSourcePredicate.CAN_PROTECT.getLoader());
        DamageSourcePredicate.LOADER.register(getResource("message"), SourceMessagePredicate.LOADER);
        DamageSourcePredicate.LOADER.register(getResource("attacker"), SourceAttackerPredicate.LOADER);
      }
    }
    else if (key == Registries.BLOCK_ENTITY_TYPE) {
      BlockEntityTypeRegistryAdapter adapter = new BlockEntityTypeRegistryAdapter(Objects.requireNonNull(event.getForgeRegistry()));
      adapter.register(MantleSignBlockEntity::new, "sign", MantleSignBlockEntity::buildSignBlocks);
    }
    else if (key == ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS) {
      MantleLoot.registerGlobalLootModifiers(event);
    }
  }

  private void gatherData(final GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    boolean server = event.includeServer();
    boolean client = event.includeClient();
    PackOutput packOutput = generator.getPackOutput();
    generator.addProvider(server, new MantleFluidTagProvider(packOutput, event.getLookupProvider(), event.getExistingFileHelper()));
    generator.addProvider(client, new MantleFluidTooltipProvider(packOutput));
  }

  /**
   * Gets a resource location for Mantle
   * @param name  Name
   * @return  Resource location instance
   */
  public static ResourceLocation getResource(String name) {
    return new ResourceLocation(modId, name);
  }

  /**
   * Makes a translation key for the given name
   * @param base  Base name, such as "block" or "gui"
   * @param name  Object name
   * @return  Translation key
   */
  public static String makeDescriptionId(String base, String name) {
    return Util.makeDescriptionId(base, getResource(name));
  }

  /**
   * Makes a translation text component for the given name
   * @param base  Base name, such as "block" or "gui"
   * @param name  Object name
   * @return  Translation key
   */
  public static MutableComponent makeComponent(String base, String name) {
    return Component.translatable(makeDescriptionId(base, name));
  }
}
