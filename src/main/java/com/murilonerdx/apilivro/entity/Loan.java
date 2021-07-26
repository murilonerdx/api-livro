package com.murilonerdx.apilivro.entity;

import java.time.LocalDate;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
@Builder
@Entity
public class Loan {
  private Long id;
  private String customer;
  private Book book;
  private LocalDate loanDate;
  private Boolean returned;
}
