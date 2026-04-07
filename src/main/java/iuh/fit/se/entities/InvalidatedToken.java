package iuh.fit.se.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class InvalidatedToken {
    @Id
    String id; // Lưu JWT ID (JTI) của Token
    Date expiryTime; // Để sau này có thể viết job tự động dọn rác các token đã hết hạn
}