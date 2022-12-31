package pt.meetlisbon.backend.configurations;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class ServerUserDetails implements UserDetailsService {
/*
UserDetailsService has been used here to simplify the process with JWT claims.
TODO: implement our own
*/
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findUserByUsrNameEquals(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' was not found", username));
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(user.getUsrPasswordHash())
                .authorities(user.getUsrRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}