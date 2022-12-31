package pt.meetlisbon.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.meetlisbon.backend.configurations.JwtTokenProvider;
import pt.meetlisbon.backend.exceptions.NotFoundException;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.entities.UserRole;
import pt.meetlisbon.backend.models.repository.UserRepository;

import java.util.Collections;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String refresh(String username) {
        User user = userRepository.findUserByUsrNameEquals(username);
        if(user == null) throw new NotFoundException("User");
        UserRole userRole = user.getUsrRole();
        return jwtTokenProvider.createToken(username,
                Collections.singletonList(userRole));
    }
}