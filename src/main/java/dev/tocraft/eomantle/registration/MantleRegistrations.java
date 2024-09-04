package dev.tocraft.eomantle.registration;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;
import dev.tocraft.eomantle.Mantle;
import dev.tocraft.eomantle.block.entity.MantleSignBlockEntity;

import static dev.tocraft.eomantle.registration.RegistrationHelper.injected;

/**
 * Various objects registered under Mantle
 */
public class MantleRegistrations {
  private MantleRegistrations() {}

  @ObjectHolder(registryName = "minecraft:block_entity_type", value = Mantle.modId+":sign")
  public static final BlockEntityType<MantleSignBlockEntity> SIGN = injected();
}
