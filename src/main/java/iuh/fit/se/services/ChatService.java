package iuh.fit.se.services;

import iuh.fit.se.dtos.request.SendMessageRequest;
import iuh.fit.se.dtos.response.ConversationResponse;
import iuh.fit.se.dtos.response.MessageResponse;

import java.util.List;

public interface ChatService {
    List<ConversationResponse> getAllConversations();
    ConversationResponse getConversationById(String conversationId);
    List<MessageResponse> getMessagesByConversationId(String conversationId);
    MessageResponse sendMessage(String conversationId, SendMessageRequest request);
}