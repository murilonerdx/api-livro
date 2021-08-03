package com.murilonerdx.apilivro.service;

import com.murilonerdx.apilivro.entity.Loan;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

  private static final String CRON_LATE_LOANS = "0 0 0 1/1 * ?";

  @Value("${application.mail.lateloans.message}")
  private String message;

  private final LoanService service;

  private final EmailService emailService;

  @Scheduled(cron = CRON_LATE_LOANS)
  public void sendMailToLateLoans(){
    List<Loan> allLateLoans = service.getAllLateLoans();
    List<String> mailsList = allLateLoans.stream().map(Loan::getCustomerEmail)
        .collect(Collectors.toList());


    emailService.sendMails(message, mailsList);
  }

}
