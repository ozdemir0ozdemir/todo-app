package ozdemir0ozdemir.todoappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ozdemir0ozdemir.todoappbackend.model.Authority;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeResponse {

    private String username;
    private String token;
    private List<Authority> authorities;
}
