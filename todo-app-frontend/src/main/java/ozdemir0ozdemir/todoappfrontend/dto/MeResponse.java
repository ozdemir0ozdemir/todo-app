package ozdemir0ozdemir.todoappfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeResponse {

    private String username;
    private String token;
    private List<AuthorityDto> authorities;
}
