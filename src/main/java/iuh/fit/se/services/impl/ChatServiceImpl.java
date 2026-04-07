package iuh.fit.se.services.impl;

import iuh.fit.se.dtos.request.SendMessageRequest;
import iuh.fit.se.dtos.response.ConversationResponse;
import iuh.fit.se.dtos.response.MessageResponse;
import iuh.fit.se.entities.Conversation;
import iuh.fit.se.entities.Message;
import iuh.fit.se.entities.User;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.entities.enums.MessageStatus;
import iuh.fit.se.exceptions.AppException;
import iuh.fit.se.mapper.ConversationMapper;
import iuh.fit.se.mapper.MessageMapper;
import iuh.fit.se.repositories.ConversationRepository;
import iuh.fit.se.repositories.MessageRepository;
import iuh.fit.se.repositories.UserRepository;
import iuh.fit.se.services.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatServiceImpl implements ChatService {

    ConversationRepository conversationRepository;
    MessageRepository messageRepository;
    UserRepository userRepository;
    ConversationMapper conversationMapper;
    MessageMapper messageMapper;

    @Override
    public List<ConversationResponse> getAllConversations() {
        return conversationRepository.findAll().stream()
                .map(conversationMapper::toConversationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationResponse getConversationById(String conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND));
        return conversationMapper.toConversationResponse(conversation);
    }

    @Override
    public List<MessageResponse> getMessagesByConversationId(String conversationId) {
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId).stream()
                .map(messageMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse sendMessage(String conversationId, SendMessageRequest request) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND));

        // Get current user from security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User sender = userRepository.findUserByEmail(email);
        if (sender == null) {
            throw new AppException(HttpCode.NOT_FOUND);
        }

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .type(request.getType())
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .status(MessageStatus.SENT)
                .caption(request.getCaption())
                .fileName(request.getFileName())
                .fileSize(request.getFileSize())
                .build();

        message = messageRepository.save(message);

        // Update conversation last message
        conversation.setLastMessage(message.getContent());
        conversation.setLastMessageTime(message.getTimestamp());
        conversationRepository.save(conversation);

        return messageMapper.toMessageResponse(message);
    }
}