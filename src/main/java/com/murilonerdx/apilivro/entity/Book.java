package com.murilonerdx.apilivro.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
@Builder
@Entity
public class Book implements Serializable {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String author;
  private String isbn;
  @OneToMany(mappedBy="book", fetch = FetchType.LAZY)
  private List<Loan> loans;

}

