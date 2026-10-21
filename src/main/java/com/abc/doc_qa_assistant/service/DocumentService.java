package com.abc.doc_qa_assistant.service;

import com.abc.doc_qa_assistant.config.VectorStoreConfig;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    private final SimpleVectorStore vectorStore;

    public DocumentService(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public int ingest(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        // 1. Read: Tika extracts text from PDF / DOCX / TXT
        List<Document> documents =
                new TikaDocumentReader(new ByteArrayResource(file.getBytes())).get();

        // 2. Split into chunks (default size suits real documents)
        //List<Document> chunks = new TokenTextSplitter().apply(documents);
        List<Document> chunks = TokenTextSplitter.builder().build().apply(documents);

        // 3. Tag each chunk with its file name, so answers can show sources
        List<Document> tagged = chunks.stream()
                .map(c -> new Document(c.getText(), Map.of("fileName", fileName)))
                .toList();

        // 4. Embed + store, then save to disk
        vectorStore.add(tagged);
        VectorStoreConfig.STORE_FILE.getParentFile().mkdirs();
        vectorStore.save(VectorStoreConfig.STORE_FILE);

        return tagged.size();
    }
}