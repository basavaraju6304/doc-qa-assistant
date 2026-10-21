package com.abc.doc_qa_assistant.controller;

import com.abc.doc_qa_assistant.dto.AnswerResponse;
import com.abc.doc_qa_assistant.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public AnswerResponse ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}