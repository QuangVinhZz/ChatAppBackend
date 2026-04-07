package iuh.fit.se.entities;

import iuh.fit.se.entities.enums.ConversationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "conversations")
@SuperBuilder
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    ConversationType type;

    String name;
    String avatar;
    String lastMessage;
    LocalDateTime lastMessageTime;
    Integer unreadCount; // Note: This might need to be per user, but for simplicity

    @ManyToMany
    @JoinTable(
            name = "conversation_participants",
            joinColumns = @JoinColumn(name = "conversation_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> participants = new HashSet<>();

    @OneToOne(mappedBy = "conversation")
    Group group;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Message> messages = new HashSet<>();
}