package com.murilonerdx.apilivro.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.service.BookService;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import com.murilonerdx.apilivro.service.LoanService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc
public class BookControllerTest {

  static String BOOK_API = "/api/books";

  @Autowired
  MockMvc mvc;

  @MockBean
  BookService service;

  @MockBean
  LoanService loanService;

  @Test
  @DisplayName("Deve criar um livro com sucesso.")
  public void createBookTest() throws Exception {
    Book build = Book.builder().id(1L).author("Murilo").title("Meu livro").isbn("12345").build();
    BookDTO dto = createBookDTO();
    BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(build);
    String json = new ObjectMapper().writeValueAsString(dto);

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("id").value(1L))
        .andExpect(jsonPath("title").value("Meu livro"))
        .andExpect(jsonPath("author").value("Murilo"))
        .andExpect(jsonPath("isbn").value("12345"));
  }

  private Book createNewBook() {
    return Book.builder().author("Murilo").title("Meu livro").isbn("12345").build();
  }

  @Test
  @DisplayName("Deve lan??ar erro de valida????o quando n??o houver dados suficiente para cria????o do livro.")
  public void createInvalidBookTest() throws Exception {

    String json = new ObjectMapper().writeValueAsString(new BookDTO());
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)));

  }

  @Test
  @DisplayName("Deve lan??ar um erro caso tente cadastrar um Book com isbn j?? cadastrado")
  public void createBookWithDuplicatedIsbn() throws Exception {

    String mensagemDeErro = "ISBN j?? cadastrado";

    BookDTO bookDTO = createBookDTO();
    String json = new ObjectMapper().writeValueAsString(bookDTO);
    BDDMockito.given(service.save(Mockito.any(Book.class)))
        .willThrow(new BusinessException(mensagemDeErro));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", Matchers.hasSize(1)))
        .andExpect(jsonPath("errors[0]").value(mensagemDeErro));

  }

  private BookDTO createBookDTO() {
    return BookDTO.builder().author("Murilo").title("Meu livro").isbn("12345").build();
  }

  @Test
  @DisplayName("Deve obter infoma????o de um livro.")
  public void getBookDetailsTest() throws Exception {
    Long id = 1L;
    Book book = Book.builder().id(id).author("Murilo").isbn("12345").title("Meu livro").build();
    BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .get(BOOK_API.concat("/" + id))
        .accept(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(1L))
        .andExpect(jsonPath("title").value("Meu livro"))
        .andExpect(jsonPath("author").value("Murilo"))
        .andExpect(jsonPath("isbn").value("12345"));

  }

  @Test
  @DisplayName("Deve retornar resources not found quando o livro procurado n??o existir")
  public void bookNotFoundTest() throws Exception {
    BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .get(BOOK_API.concat("/" + 1))
        .accept(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deletar um livro")
  public void deleteBookTest() throws Exception {
    BDDMockito.given(service.getById(anyLong()))
        .willReturn(Optional.of(Book.builder().id(1L).build()));
    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .delete(BOOK_API.concat("/" + 1))
        .accept(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Atualizar um livro")
  public void updateBookTest() throws Exception {
    Long id = 1L;
    String json = new ObjectMapper().writeValueAsString(createBookDTO());

    Book updatingBook = Book.builder().id(id).title("Outro titulo").author("Outro autor")
        .isbn("12345").build();

    BDDMockito.given(service.getById(id)).willReturn(Optional.of(updatingBook));
    Book updatedBook = Book.builder().id(id).title("Meu livro").author("Murilo").isbn("12345")
        .build();
    BDDMockito.given(service.update(updatingBook)).willReturn(updatedBook);

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .put(BOOK_API.concat("/" + 1))
        .content(json)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("id").value(1L))
        .andExpect(jsonPath("title").value("Meu livro"))
        .andExpect(jsonPath("author").value("Murilo"))
        .andExpect(jsonPath("isbn").value("12345"));
  }

  @Test
  @DisplayName("Deve retornar 404 ao tentar atualizar um livro inexistente")
  public void updateInexistentBookTest() throws Exception {
    String json = new ObjectMapper().writeValueAsString(createBookDTO());

    BDDMockito.given(service.getById(anyLong())).willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .put(BOOK_API.concat("/" + 1))
        .content(json)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deve filtrar livros")
  public void findBookTest() throws Exception {
    Long id = 1L;
    Book book = Book.builder()
        .id(id)
        .title(createNewBook().getTitle())
        .author(createBookDTO().getAuthor())
        .isbn(createBookDTO().getIsbn())
        .build();

    BDDMockito.given(service.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
        .willReturn(new PageImpl<Book>(Collections.singletonList(book), PageRequest.of(0, 100), 1));
    String queryString = String
        .format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat(queryString))
        .accept(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("content", Matchers.hasSize(1)))
        .andExpect(jsonPath("totalElements").value(1))
        .andExpect(jsonPath("pageable.pageSize").value(100))
        .andExpect(jsonPath("pageable.pageNumber").value(0));
  }


}
