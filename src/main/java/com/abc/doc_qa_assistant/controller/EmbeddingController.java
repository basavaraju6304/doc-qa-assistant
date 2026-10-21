package com.abc.doc_qa_assistant.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/api/embed")
    public String embed(@RequestParam String text) {
        float[] vector = embeddingModel.embed(text);
        return "Your text became " + vector.length + " numbers";
    }
}