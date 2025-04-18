package com.sellular.sampledropwizard.domain.dtos.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class CreateUserRequest {

    @NotBlank (message = "cannot be null or empty or whitespaces")
    private String username;

    private String email;

    private String contact;

}
