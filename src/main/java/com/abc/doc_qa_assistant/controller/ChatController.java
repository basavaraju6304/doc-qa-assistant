package com.abc.doc_qa_assistant.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.abc.doc_qa_assistant.model.TopicExplanation;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/explain")
    public TopicExplanation explain(@RequestParam String topic) {
        return chatClient.prompt()
                .system("You are a helpful teacher for beginner Java developers. Keep answers short and simple.")
                .user(u -> u.text("Explain the topic: {topic}").param("topic", topic))
                .call()
                .entity(TopicExplanation.class);
    }
}