package it.salsi.upload.services;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static it.salsi.upload.configs.StorageConfigs.Constant.*;


@Log
public class StorageServiceImpl implements StorageService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${server.file-path-persistence}")
    private String filePathPersistenceFromConf;

    private Path filePath;

    @Override
    public void init() {

        Optional.ofNullable(filePathPersistenceFromConf).ifPresentOrElse(s -> filePath = Path.of(filePathPersistenceFromConf), () -> filePath = Path.of(FILE_PATH_DEFAULT.value));
        if (!filePath.toFile().exists()) {
            if (!filePath.toFile().mkdirs()) {
                log.severe(filePath.getRoot() + " not created");
            }
        }

        log.info(filePath.toFile().getAbsolutePath());
    }

    @Override
    public void store(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new IOException("filename null");
        }

        file.transferTo(new File(filePath.toFile().getAbsoluteFile(), file.getOriginalFilename()));
    }

    @Override
    public Stream<Path> loadAll() throws IOException {
        return Files.list(filePath);
    }

    @Override
    public Path load(String filename) {
        return new File(filePath.toFile(), filename).toPath();
    }

    @Override
    public Resource loadAsResource(String filename) {
        return resourceLoader.getResource("classpath:data/" + filename);
    }

    @Override
    public void deleteAll() {
        if (filePath == null) {
            return;
        }
        Arrays.asList(filePath.toFile().listFiles()).forEach(File::delete);
    }
}
