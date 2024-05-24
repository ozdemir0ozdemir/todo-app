package ozdemir0ozdemir.todoappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskPageResponse{

    private TaskResponse taskResponse;
    private Long totalTaskCount;
    private Integer currentPage;
    private Integer totalPage;
}
