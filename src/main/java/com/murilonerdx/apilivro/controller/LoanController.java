package com.murilonerdx.apilivro.controller;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.dto.LoanDTO;
import com.murilonerdx.apilivro.dto.LoanFilterDTO;
import com.murilonerdx.apilivro.dto.ReturnedLoanDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.service.BookService;
import com.murilonerdx.apilivro.service.LoanService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
  private final ModelMapper modelMapper;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Long create(@RequestBody LoanDTO dto){
    Book book = bookService.getBookByIsbn(dto.getIsbn()).orElseThrow(()->new ResponseStatusException(HttpStatus.BAD_REQUEST,"Book not found for isbn"));
    Loan entity = Loan.builder().book(book).customer(dto.getCustomer()).loanDate(LocalDate.now()).build();

    entity = service.save(entity);
    return entity.getId();
  }

  @PatchMapping("/{id}")
  public void returnBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto){
      Loan loan = service.getById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
      loan.setReturned(dto.getReturned());
      service.update(loan);
  }

  @GetMapping
  public Page<LoanDTO> find(LoanFilterDTO dto, Pageable pageRequest){
    Page<Loan> result = service.find(dto, pageRequest);
    List<LoanDTO> loanDTOList = result.getContent().stream().map(entity -> {
      Book book = entity.getBook();
      BookDTO bookDTO = modelMapper.map(book, BookDTO.class);
      LoanDTO loanDTO = modelMapper.map(entity, LoanDTO.class);
      loanDTO.setBook(bookDTO);
      return loanDTO;
    }).collect(
        Collectors.toList());
    return new PageImpl<LoanDTO>(loanDTOList, pageRequest, result.getTotalElements()) {
    };
  }

}
