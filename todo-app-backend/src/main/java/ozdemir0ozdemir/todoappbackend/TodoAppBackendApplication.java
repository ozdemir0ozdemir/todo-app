package ozdemir0ozdemir.todoappbackend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ozdemir0ozdemir.todoappbackend.model.Authority;
import ozdemir0ozdemir.todoappbackend.model.Member;
import ozdemir0ozdemir.todoappbackend.repository.AuthorityRepository;
import ozdemir0ozdemir.todoappbackend.repository.MemberRepository;

import java.util.List;

@SpringBootApplication
public class TodoAppBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner addNewRows(AuthorityRepository authorityRepository, MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        return (args)->{
            Authority a1 = Authority.builder().authority("MEMBER").build();
            Authority a2 = Authority.builder().authority("MANAGER").build();
            Authority a3 = Authority.builder().authority("ADMIN").build();

            a1 = authorityRepository.save(a1);
            a2 = authorityRepository.save(a2);
            a3 = authorityRepository.save(a3);

            Member m1 = Member.builder()
                    .authorities(List.of(a1,a2,a3))
                    .isAccountExpired(false)
                    .isAccountLocked(false)
                    .isCredentialsExpired(false)
                    .isEnabled(true)
                    .password(passwordEncoder.encode("password"))
                    .username("admin")
                    .build();
            memberRepository.save(m1);

            Member m2 = Member.builder()
                    .authorities(List.of(a1,a2))
                    .isAccountExpired(false)
                    .isAccountLocked(false)
                    .isCredentialsExpired(false)
                    .isEnabled(true)
                    .password(passwordEncoder.encode("password"))
                    .username("manager")
                    .build();
            memberRepository.save(m2);

            Member m3 = Member.builder()
                    .authorities(List.of(a1))
                    .isAccountExpired(false)
                    .isAccountLocked(false)
                    .isCredentialsExpired(false)
                    .isEnabled(true)
                    .password(passwordEncoder.encode("password"))
                    .username("member")
                    .build();
            memberRepository.save(m3);

        };
    }

}
