package com.murilonerdx.apilivro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.impl.LoanServiceImpl;
import com.murilonerdx.apilivro.repository.LoanRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

  LoanService service;

  @MockBean
  LoanRepository repository;

  @BeforeEach
  public void setUp(){
    this.service = new LoanServiceImpl(repository);
  }

  @Test
  @DisplayName("Deve salvar um empréstimo")
  public void saveLoanTest(){
    String customer = "Murilo";
    Book book = Book.builder()
        .id(1L)
        .author(customer)
        .title("Meu livro")
        .isbn("12345")
        .build();

    Loan saveingLoan = Loan.builder()
        .book(book)
        .customer(customer)
        .loanDate(LocalDate.now())
        .build();

    Loan savedLoan = Loan.builder()
        .book(book)
        .loanDate(LocalDate.now())
        .customer(customer)
        .book(book).build();

    when(repository.save(saveingLoan)).thenReturn(savedLoan);

    Loan loan = service.save(savedLoan);
    assertThat(loan.getId()).isEqualTo(savedLoan.getId());
    assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
    assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
    assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
  }

}
