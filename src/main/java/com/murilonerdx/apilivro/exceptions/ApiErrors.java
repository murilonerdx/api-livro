package com.murilonerdx.apilivro.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

public class ApiErrors {
  private List<String> errors;

  public ApiErrors(BindingResult bindingResult){
    this.errors = new ArrayList<>();
    bindingResult.getAllErrors().forEach(error->this.errors.add(error.getDefaultMessage()));
  }

  public ApiErrors(BusinessException e) {
    this.errors = (Collections.singletonList(e.getMessage()));
  }

  public ApiErrors(IllegalArgumentException e) {
    this.errors = (Collections.singletonList(e.getMessage()));
  }

  public ApiErrors(ResponseStatusException e) {
    this.errors = Collections.singletonList(e.getReason());
  }

  public List<String> getErrors() {
    return errors;
  }
}
