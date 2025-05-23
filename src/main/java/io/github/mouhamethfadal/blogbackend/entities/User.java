package io.github.mouhamethfadal.blogbackend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseAuditableEntity {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private boolean active;
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
