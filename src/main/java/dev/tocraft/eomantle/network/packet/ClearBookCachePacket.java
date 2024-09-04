package dev.tocraft.eomantle.network.packet;

import dev.tocraft.eomantle.command.ClearBookCacheCommand;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import dev.tocraft.eomantle.client.book.BookLoader;
import dev.tocraft.eomantle.client.book.data.BookData;
import dev.tocraft.eomantle.network.packet.OpenNamedBookPacket.ClientOnly;

import javax.annotation.Nullable;

/** Packet sent by {@link ClearBookCacheCommand} to reset a book cache */
public record ClearBookCachePacket(@Nullable ResourceLocation book) implements IThreadsafePacket {
  public ClearBookCachePacket(FriendlyByteBuf buffer) {
    this(buffer.readBoolean() ? buffer.readResourceLocation() : null);
  }

  @Override
  public void encode(FriendlyByteBuf buf) {
    if (book != null) {
      buf.writeBoolean(true);
      buf.writeResourceLocation(book);
    } else {
      buf.writeBoolean(false);
    }
  }

  @Override
  public void handleThreadsafe(NetworkEvent.Context context) {
    if (book != null) {
      BookData bookData = BookLoader.getBook(book);
      if (bookData != null) {
        bookData.reset();
      } else {
        ClientOnly.errorStatus(book);
      }
    } else {
      BookLoader.resetAllBooks();
    }
  }
}
