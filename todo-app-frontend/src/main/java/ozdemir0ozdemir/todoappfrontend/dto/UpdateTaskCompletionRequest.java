package ozdemir0ozdemir.todoappfrontend.dto;

import jakarta.validation.constraints.NotNull;
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
