package com.abc.doc_qa_assistant.controller;

import com.abc.doc_qa_assistant.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/docs")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();

        if (file.isEmpty() || name == null
                || !name.toLowerCase().matches(".*\\.(pdf|txt|docx)$")) {
            return ResponseEntity.badRequest()
                    .body("Please upload a non-empty PDF, TXT or DOCX file");
        }

        int chunks = documentService.ingest(file);
        return ResponseEntity.ok("Stored " + chunks + " chunks from " + name);
    }
}