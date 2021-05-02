package ies.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoConfiguration
@SpringBootApplication
public class ProjetoApplication {
   
    public static void main(String[] args) {
        SpringApplication.run(ProjetoApplication.class, args);
    }

}
