package com.abc.doc_qa_assistant.service;

import com.abc.doc_qa_assistant.dto.AnswerResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final String NOT_FOUND = "I couldn't find that in the uploaded documents.";

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    public AnswerResponse ask(String question) {

        // 1. RETRIEVE the most relevant chunks
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(4).build());

        if (docs.isEmpty()) {
            return new AnswerResponse(NOT_FOUND, List.of());
        }

        String context = docs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n---\n"));

        // 2. AUGMENT the prompt with the chunks, 3. GENERATE the answer
        String answer = chatClient.prompt()
                .system("""
                        You answer questions using ONLY the context provided.
                        If the answer is not in the context, reply exactly:
                        "%s"
                        Keep answers short and clear.
                        Reply in plain text only. Do not use Markdown, asterisks or bullet symbols.
                        """.formatted(NOT_FOUND))
                .user(u -> u.text("""
                        Context:
                        {context}

                        Question: {question}
                        """)
                        .param("context", context)
                        .param("question", question))
                .call()
                .content();

        // Show sources only when the answer really came from the documents
        if (answer == null || answer.contains("couldn't find")) {
            return new AnswerResponse(NOT_FOUND, List.of());
        }

        List<String> sources = docs.stream()
                .map(d -> String.valueOf(d.getMetadata().get("fileName")))
                .distinct()
                .toList();

        return new AnswerResponse(answer, sources);
    }
}