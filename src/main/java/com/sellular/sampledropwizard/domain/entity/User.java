package com.sellular.sampledropwizard.domain.entity;

import com.sellular.commons.jpa.entity.ExternalBase;
import com.sellular.sampledropwizard.domain.dtos.request.CreateUserRequest;
import com.sellular.sampledropwizard.domain.dtos.request.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class User extends ExternalBase {

    @Column(name = "username")
    private String username;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "contact", nullable = true)
    private String contact;

    public static User from(final CreateUserRequest request) {
        User.UserBuilder userBuilder = User.builder()
                .username(request.getUsername());
        Optional.ofNullable(request.getContact()).ifPresent(userBuilder::contact);
        Optional.ofNullable(request.getEmail()).ifPresent(userBuilder::email);
        return userBuilder.build();
    }

    public void merge(final UpdateUserRequest request) {
        Optional.ofNullable(request.getUsername()).ifPresent(this::setUsername);
        Optional.ofNullable(request.getContact()).ifPresent(this::setContact);
        Optional.ofNullable(request.getEmail()).ifPresent(this::setEmail);
    }

}
