package com.abc.doc_qa_assistant.dto;

import java.util.List;

public record AnswerResponse(String answer, List<String> sources) {
}