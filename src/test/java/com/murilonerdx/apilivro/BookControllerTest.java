package com.murilonerdx.apilivro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {

  static String BOOK_API = "/api/books";

  @Autowired
  MockMvc mvc;

  @MockBean
  BookService service;


  @Test
  @DisplayName("Deve criar um livro com sucesso.")
  public void createBookTest() throws Exception {
    Book build = Book.builder().author("Murilo").title("Meu livro").isbn("1234").build();
    BookDTO dto = BookDTO.builder().author("Murilo").title("Meu livro").isbn("1234").build();
    BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(build);
    String json = new ObjectMapper().writeValueAsString(dto);


    MockHttpServletRequestBuilder request = MockMvcRequestBuilders
        .post(BOOK_API)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("title").value("Meu livro"))
        .andExpect(MockMvcResultMatchers.jsonPath("author").value("Murilo"))
        .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("12345"));
  }

  @Test
  @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do livro.")
  public void createInvalidBookTest(){

  }
}
