package ozdemir0ozdemir.todoappbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull
    private Boolean completed;
}
