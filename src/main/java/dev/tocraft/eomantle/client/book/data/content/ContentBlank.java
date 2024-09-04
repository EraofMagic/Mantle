package dev.tocraft.eomantle.client.book.data.content;

import net.minecraft.resources.ResourceLocation;
import dev.tocraft.eomantle.Mantle;
import dev.tocraft.eomantle.client.book.data.BookData;
import dev.tocraft.eomantle.client.screen.book.element.BookElement;

import java.util.ArrayList;

public class ContentBlank extends PageContent {
  public static final ResourceLocation ID = Mantle.getResource("blank");

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
  }
}
