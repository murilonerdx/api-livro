package com.murilonerdx.apilivro.dto;

import com.sun.istack.NotNull;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookDTO implements Serializable {
  private Long id;
  @NotEmpty
  private String title;
  @NotEmpty
  private String author;
  @NotEmpty
  private String isbn;
}
