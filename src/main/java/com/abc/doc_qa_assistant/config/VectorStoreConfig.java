package com.abc.doc_qa_assistant.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class VectorStoreConfig {

    public static final File STORE_FILE = new File("data/vectorstore.json");

    @Bean
    public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();
        if (STORE_FILE.exists()) {
            store.load(STORE_FILE);
        }
        return store;
    }
}