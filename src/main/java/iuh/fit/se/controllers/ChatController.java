package iuh.fit.se.controllers;

import iuh.fit.se.dtos.request.SendMessageRequest;
import iuh.fit.se.dtos.response.ApiResponse;
import iuh.fit.se.dtos.response.ConversationResponse;
import iuh.fit.se.dtos.response.MessageResponse;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.services.ChatService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat-management/api/v1/chat")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ChatController {

    ChatService chatService;

    @GetMapping("/conversations")
    ApiResponse<List<ConversationResponse>> getAllConversations() {
        return ApiResponse.<List<ConversationResponse>>builder()
                .code(HttpCode.OK.getCODE())
                .message(HttpCode.OK.getMESSAGE())
                .data(chatService.getAllConversations())
                .build();
    }

    @GetMapping("/conversations/{conversationId}")
    ApiResponse<ConversationResponse> getConversationById(@PathVariable String conversationId) {
        return ApiResponse.<ConversationResponse>builder()
                .code(HttpCode.OK.getCODE())
                .message(HttpCode.OK.getMESSAGE())
                .data(chatService.getConversationById(conversationId))
                .build();
    }

    @GetMapping("/conversations/{conversationId}/messages")
    ApiResponse<List<MessageResponse>> getMessages(@PathVariable String conversationId) {
        return ApiResponse.<List<MessageResponse>>builder()
                .code(HttpCode.OK.getCODE())
                .message(HttpCode.OK.getMESSAGE())
                .data(chatService.getMessagesByConversationId(conversationId))
                .build();
    }

    @PostMapping("/conversations/{conversationId}/messages")
    ApiResponse<MessageResponse> sendMessage(@PathVariable String conversationId, @Valid @RequestBody SendMessageRequest request) {
        return ApiResponse.<MessageResponse>builder()
                .code(HttpCode.CREATED.getCODE())
                .message("Message sent successfully")
                .data(chatService.sendMessage(conversationId, request))
                .build();
    }
}