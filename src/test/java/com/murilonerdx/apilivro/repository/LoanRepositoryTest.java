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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    Book book = createAndpersistBook();

    //execucao
    boolean exists = repository.existsByBookAndNotReturn(book);
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Deve buscar emprestimo pelo isbn do livro ou customer")
  public void findByBookIsbnOrCustomer(){
    //cenario
    Book book = createAndpersistBook();

    //execucao
    Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Murilo", PageRequest.of(0, 10));
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getPageable().getPageSize()).isEqualTo(10);
    assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
    assertThat(result.getTotalElements()).isEqualTo(1);

  }

  private Book createAndpersistBook() {
    Book book = createNewBook("123");
    entityManager.persist(book);

    Loan loan = Loan.builder().book(book).customer("Murilo").loanDate(LocalDate.now()).build();
    entityManager.persist(loan);
    return book;
  }
}
