package iuh.fit.se.entities;

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
@Table(name = "`groups`")
@SuperBuilder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String name;
    String description;
    String avatar;
    String color;

    LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "created_by")
    User createdBy;

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> members = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "group_admins",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> admins = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "conversation_id")
    Conversation conversation;
}