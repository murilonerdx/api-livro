package com.murilonerdx.apilivro.controller;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.dto.LoanDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.exceptions.ApiErrors;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.service.BookService;
import com.murilonerdx.apilivro.service.LoanService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

  private final BookService service;
  private final ModelMapper modelMapper;
  private final LoanService loanService;


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation("Creates a book")
  public BookDTO create(@RequestBody @Valid BookDTO dto) {
//    Book entity = Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build();
    Book entity = modelMapper.map(dto, Book.class);
    entity = service.save(entity);
//    return BookDTO.builder().id(entity.getId()).author("Murilo").title("Meu livro").isbn("1234").build();
    return modelMapper.map(entity, BookDTO.class);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation("Update a book")
  public BookDTO update(@PathVariable Long id, @RequestBody BookDTO book) {
    Book obj = service.getById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    obj.setAuthor(book.getAuthor());
    obj.setTitle(book.getTitle());
    Book bookUpdated = service.update(obj);
    return modelMapper.map(bookUpdated, BookDTO.class);
  }

  @GetMapping("/{id}")
  @ApiOperation("Obtains a book details by id")
  public BookDTO getById(@PathVariable Long id) {
    Book obj = service.getById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return modelMapper.map(obj, BookDTO.class);
  }


  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiOperation("Deletes a book")
  public void delete(@PathVariable Long id) {
    Book obj = service.getById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    service.delete(obj);
  }

  @GetMapping()
  @ApiOperation("Find books by params")
  public Page<BookDTO> find(BookDTO dto, Pageable page) {
    Book filter = modelMapper.map(dto, Book.class);
    Page<Book> result = service.find(filter, page);
    List<BookDTO> listResult = result.getContent()
        .stream()
        .map(entity -> modelMapper.map(entity, BookDTO.class))
        .collect(Collectors.toList());
    return new PageImpl<BookDTO>(listResult, page, result.getTotalElements());
  }

  @GetMapping("/{id}/loans")
  public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable){
    Book book = service.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Page<Loan> result = loanService.getLoansByBook(book, pageable);
    List<LoanDTO> list = result.getContent().stream().map(loan -> {
      Book loanBook = loan.getBook();
      BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
      LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
      loanDTO.setBook(bookDTO);
      return loanDTO;
    }).collect(Collectors.toList());
    return new PageImpl<LoanDTO>(list, pageable, result.getTotalElements());
  }


}
