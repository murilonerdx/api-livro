package com.murilonerdx.apilivro.repository;

import static com.murilonerdx.apilivro.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {
  @Autowired
  private LoanRepository repository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("Deve verificar se existe não devolvido para o livro")
  public void existsByBookAndNotReturn() {

    //cenario
    Book book = createNewBook("123");
    entityManager.persist(book);

    Loan loan = Loan.builder().book(book).customer("Murilo").loanDate(LocalDate.now()).build();
    entityManager.persist(loan);

    //execucao
    boolean exists = repository.existsByBookAndNotReturn(book);
    assertThat(exists).isTrue();
  }
}
