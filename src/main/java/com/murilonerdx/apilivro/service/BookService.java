package com.murilonerdx.apilivro.service;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import java.util.Optional;

public interface BookService {
 Book save(Book entity);
 Optional<Book> getById(Long id);
 void deleteBook(Long id);
 Book updateById(Long id, BookDTO book);
}
