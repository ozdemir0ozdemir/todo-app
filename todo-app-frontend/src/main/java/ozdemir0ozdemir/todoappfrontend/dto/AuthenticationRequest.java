package ozdemir0ozdemir.todoappfrontend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AuthenticationRequest {

    @NotBlank
    @Size(min = 4)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;
}