package ozdemir0ozdemir.todoappbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ozdemir0ozdemir.todoappbackend.model.Member;
import ozdemir0ozdemir.todoappbackend.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {

        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with username \"%s\" was not found", username)));
    }

}
