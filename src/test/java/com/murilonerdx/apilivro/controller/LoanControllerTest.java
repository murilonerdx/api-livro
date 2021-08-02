package com.murilonerdx.apilivro.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murilonerdx.apilivro.dto.LoanDTO;
import com.murilonerdx.apilivro.dto.LoanFilterDTO;
import com.murilonerdx.apilivro.dto.ReturnedLoanDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.service.BookService;
import com.murilonerdx.apilivro.service.LoanService;
import com.murilonerdx.apilivro.service.LoanServiceTest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc()
public class LoanControllerTest {

  static final String LOAN_API = "/api/loans";

  @MockBean
  BookService bookService;

  @Autowired
  MockMvc mvc;

  @MockBean
  private LoanService service;

  @Test
  @DisplayName("Deve realizar um emprestimo")
  public void createLoanTest() throws Exception {
    LoanDTO dto = LoanDTO.builder().isbn("123").customer("Murilo").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    Book book = Book.builder().id(1L).isbn("123").build();
    BDDMockito
        .given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));

    Loan loan = Loan.builder().id(1L).customer("Murilo").book(book).loanDate(LocalDate.now())
        .build();
    BDDMockito.given(service.save(Mockito.any(Loan.class))).willReturn(loan);

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(status().isCreated())
        .andExpect(content().string("1"));
  }

  @Test
  @DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro inexistente.")
  public void invalidIsbnCreateLoanTest() throws Exception {
    LoanDTO dto = LoanDTO.builder().isbn("123").customer("Murilo").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    BDDMockito
        .given(bookService.getBookByIsbn("123")).willReturn(Optional.empty());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", Matchers.hasSize(1)))
        .andExpect(jsonPath("errors[0]").value("Book not found for isbn"));
  }

  @Test
  @DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro emprestado")
  public void loanBookErrorOnCreateLoanTest() throws Exception {
    LoanDTO dto = LoanDTO.builder().isbn("123").customer("Murilo").build();
    String json = new ObjectMapper().writeValueAsString(dto);

    Book book = Book.builder().id(1L).isbn("123").build();
    BDDMockito
        .given(bookService.getBookByIsbn("123")).willReturn(Optional.of(book));

    BDDMockito
        .given(service.save(Mockito.any(Loan.class))).willThrow(new BusinessException("Book already loaned"));

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json);

    mvc.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errors", Matchers.hasSize(1)))
        .andExpect(jsonPath("errors[0]").value("Book already loaned"));
  }

  @Test
  @DisplayName("Deve retornar um livro")
  public void returnLoanTest() throws Exception {
    //cenario
    ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
    BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.of(Loan.builder().id(1L).build()));
    String json = new ObjectMapper().writeValueAsString(dto);

    mvc.perform(
        patch(LOAN_API.concat("/1"))
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
    ).andExpect(status().isOk());

  }

  @Test
  @DisplayName("Deve retornar 404 quando tentar devolver um livro inexistente")
  public void returnInexistentBookTest() throws Exception {
    //cenario
    ReturnedLoanDTO dto = ReturnedLoanDTO.builder().returned(true).build();
    String json = new ObjectMapper().writeValueAsString(dto);

    BDDMockito.given(service.getById(Mockito.anyLong())).willReturn(Optional.empty());

    mvc.perform(
        patch(LOAN_API.concat("/1"))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
    ).andExpect(status().isNotFound());
  }



  @Test
  @DisplayName("Deve filtrar emprestimos")
  public void findLoanTest() throws Exception {
    Long id = 1L;
    Loan loan = LoanServiceTest.createLoan();
    loan.setId(id);
    Book book = Book.builder().id(1L).isbn("12345").build();
    loan.setBook(book);

    BDDMockito.given(service.find(Mockito.any(LoanFilterDTO.class), Mockito.any(Pageable.class)))
        .willReturn(new PageImpl<Loan>(Collections.singletonList(loan), PageRequest.of(0, 100), 1));
    String queryString = String
        .format("?isbn=%s&customer=%s&page=0&size=10", book.getIsbn(), loan.getCustomer());

    MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(LOAN_API.concat(queryString))
        .accept(MediaType.APPLICATION_JSON);

    mvc.perform(request)
        .andExpect(status().isOk())
        .andExpect(jsonPath("content", Matchers.hasSize(1)))
        .andExpect(jsonPath("totalElements").value(1))
        .andExpect(jsonPath("pageable.pageSize").value(10))
        .andExpect(jsonPath("pageable.pageNumber").value(0));
  }


}
