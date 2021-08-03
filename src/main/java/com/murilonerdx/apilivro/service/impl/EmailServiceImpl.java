package com.murilonerdx.apilivro.service.impl;

import com.murilonerdx.apilivro.service.EmailService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  @Value("${application.mail.default-remetend}")
  private String remetent;

  private final JavaMailSender javaMailSender;

  @Override
  public void sendMails(String message, List<String> mailsList) {
    String[] mails = mailsList.toArray(new String[mailsList.size()]);

    SimpleMailMessage mailMessage = new SimpleMailMessage();
    mailMessage.setFrom(remetent);
    mailMessage.setSubject("Livro com empréstimo atrasado");
    mailMessage.setText(message);
    mailMessage.setTo(mails);
    javaMailSender.send(mailMessage);
  }
}
