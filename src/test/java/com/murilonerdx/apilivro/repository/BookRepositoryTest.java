package com.murilonerdx.apilivro.repository;

import com.murilonerdx.apilivro.entity.Book;
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
    Book book = Book.builder().title("Meu livro").isbn(isbn).author("Murilo").build();
    entityManager.persist(book);

    //execucao
    boolean existsByIsbn = repository.existsByIsbn(isbn);

    //verificacao
    assertThat(existsByIsbn).isTrue();
  }
}
