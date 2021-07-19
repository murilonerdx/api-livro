package com.murilonerdx.apilivro.repository;

import com.murilonerdx.apilivro.entity.Book;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {

  @Autowired
  TestEntityManager entityManager;
  @Autowired
  BookRepository repository;

  @Test
  @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o isbn informado")
  public void returnTrueWhenIsbnExists(){
    //cenario
    String isbn = "12345";
    Book book = createNewBook(isbn);
    entityManager.persist(book);

    //execucao
    boolean existsByIsbn = repository.existsByIsbn(isbn);

    //verificacao
    assertThat(existsByIsbn).isTrue();
  }

  private Book createNewBook(String isbn) {
    return Book.builder().title("Meu livro").isbn(isbn).author("Murilo").build();
  }

  @Test
  @DisplayName("Deve obter um livro por id")
  public void findByIdTest(){
    //cenario
    Book book = createNewBook("123");
    entityManager.persist(book);

    //execucao
    Optional<Book> foundBook = repository.findById(book.getId());

    //verificacao
    assertThat(foundBook.isPresent()).isTrue();
  }



}
