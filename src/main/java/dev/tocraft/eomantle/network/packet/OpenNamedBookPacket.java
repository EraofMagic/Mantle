package dev.tocraft.eomantle.network.packet;

import lombok.AllArgsConstructor;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import dev.tocraft.eomantle.client.book.BookLoader;
import dev.tocraft.eomantle.client.book.data.BookData;

@AllArgsConstructor
public class OpenNamedBookPacket implements IThreadsafePacket {
  private static final String BOOK_ERROR = "command.eomantle.book_test.not_found";
  private final ResourceLocation book;

  public OpenNamedBookPacket(FriendlyByteBuf buffer) {
    this.book = buffer.readResourceLocation();
  }

  @Override
  public void encode(FriendlyByteBuf buf) {
    buf.writeResourceLocation(book);
  }

  @Override
  public void handleThreadsafe(NetworkEvent.Context context) {
    BookData bookData = BookLoader.getBook(book);
    if(bookData != null) {
      bookData.openGui(Component.literal("Book"), "", null, null);
    } else {
      ClientOnly.errorStatus(book);
    }
  }

  static class ClientOnly {
    static void errorStatus(ResourceLocation book) {
      Player player = Minecraft.getInstance().player;
      if (player != null) {
        player.displayClientMessage(Component.translatable(BOOK_ERROR, book).withStyle(ChatFormatting.RED), false);
      }
    }
  }
}