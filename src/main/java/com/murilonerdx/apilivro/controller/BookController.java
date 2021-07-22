package com.murilonerdx.apilivro.controller;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.exceptions.ApiErrors;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.service.BookService;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BookController {

  private final BookService service;
  private ModelMapper modelMapper;

  @Autowired
  public BookController(BookService service, ModelMapper mapper) {
    this.modelMapper = mapper;
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookDTO create(@RequestBody @Valid BookDTO dto) {
//    Book entity = Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build();
    Book entity = modelMapper.map(dto, Book.class);
    entity = service.save(entity);
//    return BookDTO.builder().id(entity.getId()).author("Murilo").title("Meu livro").isbn("1234").build();
    return modelMapper.map(entity, BookDTO.class);
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO book) {
    Book obj = service.getById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    obj.setAuthor(book.getAuthor());
    obj.setTitle(book.getTitle());
    Book bookUpdated = service.update(obj);
    return modelMapper.map(bookUpdated, BookDTO.class);
  }

  @GetMapping("/{id}")
  public BookDTO getById(@PathVariable Long id) {
    Book obj = service.getById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    return modelMapper.map(obj, BookDTO.class);
  }


  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBook(@PathVariable Long id) {
    Book obj = service.getById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    service.delete(obj);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrors handleValidationExceptions(MethodArgumentNotValidException e) {
    BindingResult bindingResult = e.getBindingResult();
    return new ApiErrors(bindingResult);
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrors handleBusinessException(BusinessException e) {
    return new ApiErrors(e);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiErrors handleCannotBeNull(IllegalArgumentException e) {
    return new ApiErrors(e);
  }


}
