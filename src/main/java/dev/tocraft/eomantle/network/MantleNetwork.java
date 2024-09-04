package dev.tocraft.eomantle.network;

import net.minecraftforge.network.NetworkDirection;
import dev.tocraft.eomantle.Mantle;
import dev.tocraft.eomantle.fluid.transfer.FluidContainerTransferPacket;
import dev.tocraft.eomantle.network.packet.ClearBookCachePacket;
import dev.tocraft.eomantle.network.packet.DropLecternBookPacket;
import dev.tocraft.eomantle.network.packet.OpenLecternBookPacket;
import dev.tocraft.eomantle.network.packet.OpenNamedBookPacket;
import dev.tocraft.eomantle.network.packet.SwingArmPacket;
import dev.tocraft.eomantle.network.packet.UpdateHeldPagePacket;
import dev.tocraft.eomantle.network.packet.UpdateLecternPagePacket;

public class MantleNetwork {
  /** Network instance */
  public static final NetworkWrapper INSTANCE = new NetworkWrapper(Mantle.getResource("network"));

  /**
   * Registers packets into this network
   */
  public static void registerPackets() {
    INSTANCE.registerPacket(OpenLecternBookPacket.class, OpenLecternBookPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(UpdateHeldPagePacket.class, UpdateHeldPagePacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(UpdateLecternPagePacket.class, UpdateLecternPagePacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(DropLecternBookPacket.class, DropLecternBookPacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(SwingArmPacket.class, SwingArmPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(OpenNamedBookPacket.class, OpenNamedBookPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(FluidContainerTransferPacket.class, FluidContainerTransferPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(ClearBookCachePacket.class, ClearBookCachePacket::new, NetworkDirection.PLAY_TO_CLIENT);
  }
}
