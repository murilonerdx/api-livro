package com.murilonerdx.apilivro.impl;

import com.murilonerdx.apilivro.dto.BookDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.repository.BookRepository;
import com.murilonerdx.apilivro.service.BookService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * BookServiceImpl
 *
 * @blame Adicionando Bookservice
 */
@Service
public class BookServiceImpl implements BookService {

  private BookRepository repository;

  @Autowired
  public BookServiceImpl(BookRepository repository) {
    this.repository = repository;
  }


  @Override
  public Book save(Book entity) {
    if(repository.existsByIsbn(entity.getIsbn())){
      throw new BusinessException("Isbn já cadastrado.");
    }
    return repository.save(entity);
  }

  @Override
  public Optional<Book> getById(Long id) {
    return repository.findById(id);
  }

  @Override
  public void deleteBook(Long id) {
    repository.deleteById(id);
  }

  @Override
  public Book update(Book book) {
    return repository.save(book);
  }

  public Book updateData(Book entity, BookDTO obj){
    entity.setAuthor(obj.getAuthor());
    entity.setTitle(obj.getTitle());
    return entity;
  }
}
