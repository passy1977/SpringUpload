package it.salsi.upload;

import it.salsi.upload.services.StorageService;
import org.jetbrains.annotations.NonNls;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(@NonNls final String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @NonNls
    CommandLineRunner init(@NonNls final  StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}
