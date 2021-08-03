package com.murilonerdx.apilivro;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class ApilivroApplication {

  @Bean
  public ModelMapper modelMapper(){
    return new ModelMapper();
  }

  //cronmaker.com
  // apenas 6 digitos. 0 0 0 1 1 1
  @Scheduled(cron="0 31 17 1/1 * ?")
  public void testAgendamentoTarefas(){
    System.out.println("Agendamento de tarefas funcionando com sucesso");
  }

  public static void main(String[] args) {
    SpringApplication.run(ApilivroApplication.class, args);
  }

}
