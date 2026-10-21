package com.abc.doc_qa_assistant.model;

import java.util.List;

public record TopicExplanation(
        String title,
        String summary,
        List<String> keyPoints,
        String difficulty
) {
}