package dev.tocraft.eomantle.client.book.data.element;

import dev.tocraft.eomantle.client.book.repository.BookRepository;

public interface IDataElement {

  void load(BookRepository source);
}
