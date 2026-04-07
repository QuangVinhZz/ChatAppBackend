package iuh.fit.se.entities;

import iuh.fit.se.entities.enums.MessageStatus;
import iuh.fit.se.entities.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "messages")
@SuperBuilder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    User sender;

    @Enumerated(EnumType.STRING)
    MessageType type;

    @Column(length = 2000)
    String content;

    LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    MessageStatus status;

    String caption;
    String fileName;
    Long fileSize;
}