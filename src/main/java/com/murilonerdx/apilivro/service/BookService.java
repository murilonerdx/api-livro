package com.murilonerdx.apilivro.service;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
 Book save(Book entity);
 Optional<Book> getById(Long id);
 void delete(Book book);
 Book update(Book book);
 Page<Book> find(Book filter, Pageable pageRequest);
}
