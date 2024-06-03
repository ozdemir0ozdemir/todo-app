package ozdemir0ozdemir.todoappfrontend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDto implements GrantedAuthority {

    private Long id;
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
