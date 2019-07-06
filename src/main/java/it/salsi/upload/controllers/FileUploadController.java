package it.salsi.upload.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Collectors;

import it.salsi.upload.services.StorageService;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Log
@Controller
public final class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(@NonNls final StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    @NonNls
    public String listUploadedFiles(@NonNls final Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @NonNls
    public ResponseEntity<Resource> serveFile(@NonNls @PathVariable final String filename) {

        FileSystemResource file = new FileSystemResource(storageService.load(filename));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    @NonNls
    public String handleFileUpload(@NonNls @RequestParam("file") final MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    @NonNls
    public ResponseEntity<?> handleStorageFileNotFound(@NonNls final Exception exc) {
        log.severe(exc.getMessage());
        return ResponseEntity.notFound().build();
    }

}
