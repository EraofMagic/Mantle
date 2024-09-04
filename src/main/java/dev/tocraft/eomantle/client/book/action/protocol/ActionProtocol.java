package dev.tocraft.eomantle.client.book.action.protocol;

import dev.tocraft.eomantle.client.screen.book.BookScreen;

public abstract class ActionProtocol {
  public abstract void processCommand(BookScreen book, String param);
}
