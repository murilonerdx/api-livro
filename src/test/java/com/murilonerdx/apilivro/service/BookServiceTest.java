package com.murilonerdx.apilivro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.service.impl.BookServiceImpl;
import com.murilonerdx.apilivro.repository.BookRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    when(repository.save(book))
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
  @DisplayName("Deve lan??ar erro de negocio ao tentar salvar um livro com isbn duplicado")
  public void shoudNotSaveABookWithDuplicatedISBN() {
    //cenario
    Book book = createNewBook();
    when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
    //execucao
    Throwable exception = Assertions.catchThrowable(() -> service.save(book));

    //verificacao
    assertThat(exception)
        .isInstanceOf(BusinessException.class)
        .hasMessage("Isbn j?? cadastrado.");

    verify(repository, Mockito.never()).save(book);
  }

  @Test
  @DisplayName("Deve obter um livro por Id")
  public void getByIdTest() {
    Long id = 1L;
    Book book = createNewBook();
    book.setId(id);
    when(repository.findById(id)).thenReturn(Optional.of(book));
    Optional<Book> foundBook = service.getById(id);

    assertThat(foundBook.isPresent()).isTrue();
    assertThat(foundBook.get().getId()).isEqualTo(id);
    assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
    assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
    assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());

  }

  @Test
  @DisplayName("Deve retornar vazio ao obter um livro pod Id quando ele n??o existir")
  public void bookNotFoundGetByIdTest() {
    Long id = 1L;

    when(repository.findById(id)).thenReturn(Optional.empty());
    Optional<Book> book = service.getById(id);

    assertThat(book.isPresent()).isFalse();


  }

  @Test
  @DisplayName("Deve deletar um livro.")
  public void deleteBookTest(){
    Book book = Book.builder().id(1L).build();
    org.junit.jupiter.api.Assertions.assertDoesNotThrow(()->service.delete(book));

    verify(repository, times(1)).delete(book);
  }

  @Test
  @DisplayName("Deve ocorrer erro ao tentar deletar um livro inexistente")
  public void deleteInvalidBookTest(){
    Book book = new Book();

    org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, ()->service.delete(book));
    verify(repository, Mockito.never()).delete(book);
  }

  @Test
  @DisplayName("Deve filtrar livros pelas propriedades")
  public void findBookTest(){
    Book book = createNewBook();
    List<Book> lista = Collections.singletonList(book);
    PageRequest pageRequest = PageRequest.of(0,10);
    Page<Book> page = new PageImpl<Book>(Collections.singletonList(book), pageRequest, 1);
    when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

    Page<Book> results = service.find(book, pageRequest);

    assertThat(results.getTotalElements()).isEqualTo(1);
    assertThat(results.getContent()).isEqualTo(lista);
    assertThat(results.getPageable().getPageNumber()).isEqualTo(0);
    assertThat(results.getPageable().getPageSize()).isEqualTo(10);


  }

  @Test
  @DisplayName("Deve obter um livro pelo isbn")
  public void getBookByIsbnTest(){
    String isbn = "1230";
    when(repository.findByIsbn(isbn)).thenReturn(Optional.of(Book.builder().id(1L).isbn(isbn).build()));
    Optional<Book> book = service.getBookByIsbn(isbn);

    assertThat(book.isPresent()).isTrue();
    assertThat(book.get().getId()).isEqualTo(1L);
    assertThat(book.get().getIsbn()).isEqualTo(isbn);

    verify(repository, times(1)).findByIsbn(isbn);
  }


}
