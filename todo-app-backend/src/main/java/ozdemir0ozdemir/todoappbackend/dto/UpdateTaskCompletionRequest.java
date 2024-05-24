package ozdemir0ozdemir.todoappbackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class UpdateTaskCompletionRequest {

    @NotNull
    private Boolean completed;
}
