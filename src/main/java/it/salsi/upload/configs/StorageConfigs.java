package it.salsi.upload.configs;

import it.salsi.upload.services.StorageService;
import it.salsi.upload.services.StorageServiceImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfigs {

    public enum Constant {
        FILE_PATH_DEFAULT("files");

        public final String value;
        Constant(@NotNull final String value) {
            this.value = value;
        }
    }

    @Bean
    @NonNls
    public StorageService getStrorage() {
        return new StorageServiceImpl();
    }


}
