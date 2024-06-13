package ozdemir0ozdemir.todoappfrontend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class CreateNewTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;
}
