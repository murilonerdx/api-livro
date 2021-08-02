package com.murilonerdx.apilivro.impl;

import com.murilonerdx.apilivro.dto.LoanFilterDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.repository.LoanRepository;
import com.murilonerdx.apilivro.service.LoanService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class LoanServiceImpl implements LoanService {

  private final LoanRepository repository;

  @Autowired
  public LoanServiceImpl(LoanRepository repository) {
    this.repository = repository;
  }


  @Override
  public Loan save(Loan loan) {
    if (repository.existsByBookAndNotReturn(loan.getBook())) {
      throw new BusinessException("Book already loaned");
    }
    return repository.save(loan);
  }

  @Override
  public Optional<Loan> getById(Long id) {
    return repository.findById(id);
  }

  @Override
  public Loan update(Loan loan) {
    if (loan.getId() == null) {
      throw new BusinessException("Book id cant be null");
    }
    return repository.save(loan);
  }

  @Override
  public Page<Loan> find(LoanFilterDTO filter, Pageable pageable) {
    return repository.findByBookIsbnOrCustomer(filter.getIsbn(), filter.getCustomer(), pageable);
  }


}