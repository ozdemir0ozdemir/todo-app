package ozdemir0ozdemir.todoappfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private LocalDateTime timestamp;
    private HttpStatus status;
    private String token;
    private List<AuthorityDto> authorities;
    private String path;



}
