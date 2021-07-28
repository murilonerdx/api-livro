package com.murilonerdx.apilivro.repository;

import com.murilonerdx.apilivro.entity.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
  boolean existsByIsbn(String isbn);

  Optional<Book> findByIsbn(String isbn);
}
