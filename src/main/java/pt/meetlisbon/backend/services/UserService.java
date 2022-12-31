package pt.meetlisbon.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.meetlisbon.backend.configurations.JwtTokenProvider;
import pt.meetlisbon.backend.controllers.AuthController;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.repository.UserRepository;
import pt.meetlisbon.backend.models.requests.PatchUser;

import java.time.OffsetDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    public String signIn(String username, String password) {
            LOG.info("Trying to authenticate with user:pass -> {}:{}", username, password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, passwordEncoder.encode(password)));
            LOG.info("Starting token creation");
            return jwtTokenProvider.createToken(username,
                    Collections.singletonList(userRepository.findUserByUsrNameEquals(username).getUsrRole()));
    }

    public void partialUpdate(PatchUser newUser, User oldUser) {
        LOG.info("Updating user, these should match: {} and {}", newUser.getUsrName(), oldUser.getUsrName());
        oldUser.setUsrFirstName((newUser.getUsrFirstName() == null) ? oldUser.getUsrFirstName() : newUser.getUsrFirstName());
        oldUser.setUsrLastName((newUser.getUsrLastName() == null) ? oldUser.getUsrLastName() : newUser.getUsrLastName());
        oldUser.setUsrName((newUser.getUsrName() == null) ? oldUser.getUsrName() : newUser.getUsrName());
        oldUser.setUsrEmail((newUser.getUsrEmail() == null) ? oldUser.getUsrEmail() : newUser.getUsrEmail());
        oldUser.setUsrPasswordHash((newUser.getUsrPasswordHash() == null) ? oldUser.getUsrPasswordHash() : newUser.getUsrPasswordHash());
        oldUser.setUsrRole((newUser.getUsrRole() == null) ? oldUser.getUsrRole() : newUser.getUsrRole());
        oldUser.setUsrEmailVerified((newUser.getUsrEmailVerified() == null) ? oldUser.getUsrEmailVerified() : newUser.getUsrEmailVerified());
        oldUser.setUsrActive((newUser.getUsrActive() == null) ? oldUser.getUsrActive() : newUser.getUsrActive());
        oldUser.setUpdatedAt(OffsetDateTime.now());

        userRepository.save(oldUser);
    }

}