package com.murilonerdx.apilivro.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.impl.BookServiceImpl;
import com.murilonerdx.apilivro.repository.BookRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

  BookService service;

  @MockBean
  BookRepository repository;


  @BeforeEach
  public void setUp() {
    this.service = new BookServiceImpl(repository);
  }

  @Test
  @DisplayName("Deve salvar um livro")
  public void saveBookTest() {
    //Cenario
    Book book = Book.builder().author("Murilo").title("Meu livro").isbn("12345").build();
    Mockito.when(repository.save(book))
        .thenReturn(Book.builder()
            .id(1L).isbn("12345")
            .author("Murilo")
            .title("Meu livro").build());

    //Execucao
    Book saveBook = service.save(book);
    //Verificacao
    assertThat(saveBook.getId()).isNotNull();
    assertThat(saveBook.getIsbn()).isEqualTo("12345");
    assertThat(saveBook.getAuthor()).isEqualTo("Murilo");
    assertThat(saveBook.getTitle()).isEqualTo("Meu livro");

  }


}
