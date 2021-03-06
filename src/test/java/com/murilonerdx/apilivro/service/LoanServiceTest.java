package com.murilonerdx.apilivro.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.murilonerdx.apilivro.dto.LoanFilterDTO;
import com.murilonerdx.apilivro.entity.Book;
import com.murilonerdx.apilivro.entity.Loan;
import com.murilonerdx.apilivro.exceptions.BusinessException;
import com.murilonerdx.apilivro.service.impl.LoanServiceImpl;
import com.murilonerdx.apilivro.repository.LoanRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

  LoanService service;

  @MockBean
  LoanRepository repository;

  @BeforeEach
  public void setUp() {
    this.service = new LoanServiceImpl(repository);
  }

  @Test
  @DisplayName("Deve salvar um empréstimo")
  public void saveLoanTest() {
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

  @Test
  @DisplayName("Deve lançar erro de negócio ao salvar um emprestimo com livro já empresado")
  public void loanedBookSaveTest() {
    String customer = "Murilo";
    Book book = Book.builder()
        .id(1L)
        .author(customer)
        .title("Meu livro")
        .isbn("12345")
        .build();

    Loan savingLoan = Loan.builder()
        .book(book)
        .customer(customer)
        .loanDate(LocalDate.now())
        .build();

    when(repository.existsByBookAndNotReturn(book)).thenReturn(true);

    Throwable exception = catchThrowable(() -> service.save(savingLoan));
    assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("Book already loaned");

    verify(repository, never()).save(savingLoan);
  }

  @Test
  @DisplayName("Deve obter as informações de um emprestimo pelo id")
  public void getLoanDetailsTest() {
    //cenario
    Long id = 1L;
    Loan loan = createLoan();
    loan.setId(id);

    //execucao
    when(repository.findById(id)).thenReturn(Optional.of(loan));
    Optional<Loan> result = service.getById(id);

    //verificacao
    assertThat(result.isPresent()).isTrue();
    assertThat(result.get().getId()).isEqualTo(id);
    assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
    assertThat(result.get().getBook()).isEqualTo(loan.getBook());
    assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());
  }

  @Test
  @DisplayName("Deve atualizar um emprestimo")
  public void updateLoanTest() {
    Long id = 1L;
    Loan loan = createLoan();
    loan.setId(id);
    loan.setReturned(true);

    when(repository.save(loan)).thenReturn(loan);

    Loan updatedLoan = service.update(loan);

    assertThat(updatedLoan.getReturned()).isTrue();
    verify(repository).save(loan);
  }

  public static Loan createLoan() {
    Book book = Book.builder().id(1L).build();
    String customer = "Murilo";

    return Loan.builder()
        .book(book)
        .loanDate(LocalDate.now())
        .customer(customer)
        .book(book).build();
  }

  @Test
  @DisplayName("Deve filtrar emprestimo pelas propriedades")
  public void findLoanTest() {

    LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Murilo").isbn("12345").build();

    Loan loan = createLoan();
    loan.setId(1L);

    List<Loan> lista = Collections.singletonList(loan);
    PageRequest pageRequest = PageRequest.of(0, 10);
    Page<Loan> page = new PageImpl<Loan>(Collections.singletonList(loan), pageRequest, 1);
    when(repository.findByBookIsbnOrCustomer(Mockito.anyString(), Mockito.anyString(),
        Mockito.any(PageRequest.class))).thenReturn(page);

    Page<Loan> results = service.find(loanFilterDTO, pageRequest);

    assertThat(results.getTotalElements()).isEqualTo(1);
    assertThat(results.getContent()).isEqualTo(lista);
    assertThat(results.getPageable().getPageNumber()).isEqualTo(0);
    assertThat(results.getPageable().getPageSize()).isEqualTo(10);


  }
}
