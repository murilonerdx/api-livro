package com.murilonerdx.apilivro.controller;

import com.murilonerdx.apilivro.dto.LoanDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.service.BookService;
import com.murilonerdx.apilivro.service.LoanService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(value="/api/loans")
@RequiredArgsConstructor
public class LoanController {

  private final LoanService service;
  private final BookService bookService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody LoanDTO dto){
    Book book = bookService.getBookByIsbn(dto.getIsbn()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book not found for isbn"));
    Loan entity = Loan.builder().book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();

    entity = service.save(entity);
    return entity.getId();
  }

}
