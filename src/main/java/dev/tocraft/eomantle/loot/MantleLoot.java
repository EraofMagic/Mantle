package dev.tocraft.eomantle.loot;

import com.google.gson.JsonDeserializer;
import com.mojang.serialization.Codec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.RegisterEvent;
import dev.tocraft.eomantle.Mantle;
import dev.tocraft.eomantle.loot.condition.BlockTagLootCondition;
import dev.tocraft.eomantle.loot.condition.ContainsItemModifierLootCondition;
import dev.tocraft.eomantle.loot.condition.EmptyModifierLootCondition;
import dev.tocraft.eomantle.loot.condition.ILootModifierCondition;
import dev.tocraft.eomantle.loot.condition.InvertedModifierLootCondition;
import dev.tocraft.eomantle.loot.function.RetexturedLootFunction;
import dev.tocraft.eomantle.loot.function.SetFluidLootFunction;
import dev.tocraft.eomantle.registration.adapter.RegistryAdapter;

import java.util.Objects;

import static dev.tocraft.eomantle.loot.condition.ILootModifierCondition.MODIFIER_CONDITIONS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MantleLoot {
  /** Condition to match a block tag and property predicate */
  public static LootItemConditionType BLOCK_TAG_CONDITION;
  /** Function to add block entity texture to a dropped item */
  public static LootItemFunctionType RETEXTURED_FUNCTION;
  /** Function to add a fluid to an item fluid capability */
  public static LootItemFunctionType SET_FLUID_FUNCTION;

  /**
   * Called during serializer registration to register any relevant loot logic
   */
  public static void registerGlobalLootModifiers(final RegisterEvent event) {
    RegistryAdapter<Codec<? extends IGlobalLootModifier>> adapter = new RegistryAdapter<>(Objects.requireNonNull(event.getForgeRegistry()));
    adapter.register(AddEntryLootModifier.CODEC, "add_entry");
    adapter.register(ReplaceItemLootModifier.CODEC, "replace_item");

    // functions
    RETEXTURED_FUNCTION = registerFunction("fill_retextured_block", RetexturedLootFunction.SERIALIZER);
    SET_FLUID_FUNCTION = registerFunction("set_fluid", SetFluidLootFunction.SERIALIZER);

    // conditions
    BLOCK_TAG_CONDITION = Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, Mantle.getResource("block_tag"), new LootItemConditionType(BlockTagLootCondition.SERIALIZER));

    // loot modifier conditions
    MODIFIER_CONDITIONS.registerDeserializer(InvertedModifierLootCondition.ID, (JsonDeserializer<? extends ILootModifierCondition>)InvertedModifierLootCondition::deserialize);
    MODIFIER_CONDITIONS.registerDeserializer(EmptyModifierLootCondition.ID, EmptyModifierLootCondition.INSTANCE);
    MODIFIER_CONDITIONS.registerDeserializer(ContainsItemModifierLootCondition.ID, (JsonDeserializer<? extends ILootModifierCondition>)ContainsItemModifierLootCondition::deserialize);
  }

  /**
   * Registers a loot function
   * @param name        Loot function name
   * @param serializer  Loot function serializer
   * @return  Registered loot function
   */
  private static LootItemFunctionType registerFunction(String name, Serializer<? extends LootItemFunction> serializer) {
    return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, Mantle.getResource(name), new LootItemFunctionType(serializer));
  }
}
