package com.murilonerdx.apilivro.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.impl.BookServiceImpl;
import com.murilonerdx.apilivro.repository.BookRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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
    Book book = createNewBook();
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

  private Book createNewBook() {
    return Book.builder().author("Murilo").title("Meu livro").isbn("12345").build();
  }

  @Test
  @DisplayName("Deve lançar erro de negocio ao tentar salvar um livro com isbn duplicado")
  public void shoudNotSaveABookWithDuplicatedISBN() {
    //cenario
    Book book = createNewBook();
    Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
    //execucao
    Throwable exception = Assertions.catchThrowable(() -> service.save(book));

    //verificacao
    assertThat(exception)
        .isInstanceOf(BusinessException.class)
        .hasMessage("Isbn já cadastrado.");

    Mockito.verify(repository, Mockito.never()).save(book);
  }

  @Test
  @DisplayName("Deve obter um livro por Id")
  public void getByIdTest() {
    Long id = 1L;
    Book book = createNewBook();
    book.setId(id);
    Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));
    Optional<Book> foundBook = service.getById(id);

    assertThat(foundBook.isPresent()).isTrue();
    assertThat(foundBook.get().getId()).isEqualTo(id);
    assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
    assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());

  }

  @Test
  @DisplayName("Deve retornar vazio ao obter um livro pod Id quando ele não existir")
  public void bookNotFoundGetByIdTest() {
    Long id = 1L;

    Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
    Optional<Book> book = service.getById(id);

    assertThat(book.isPresent()).isFalse();


  }

}
