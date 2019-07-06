package it.salsi.upload.services;

import org.jetbrains.annotations.NonNls;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;


public interface StorageService {

    void init();

    void store(@NonNls final MultipartFile file) throws IOException;

    @NonNls
    Stream<Path> loadAll() throws IOException;

    @NonNls
    Path load(@NonNls final String filename);

    @NonNls
    Resource loadAsResource(@NonNls String filename);

    void deleteAll();
}
