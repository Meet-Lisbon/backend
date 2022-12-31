package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import pt.meetlisbon.backend.exceptions.*;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.repository.UserRepository;
import pt.meetlisbon.backend.models.requests.AuthMsg;
import pt.meetlisbon.backend.models.requests.GenericMsg;
import pt.meetlisbon.backend.models.requests.LoginUser;
import pt.meetlisbon.backend.models.requests.PasswordResetUser;
import pt.meetlisbon.backend.services.AuthService;
import pt.meetlisbon.backend.services.EmailSender;
import pt.meetlisbon.backend.services.RandomService;
import pt.meetlisbon.backend.services.UserService;

import javax.mail.MessagingException;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final RandomService randomService;
    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    /**
     * Calls the {@link UserService#signIn(String username, String password)}
     * responsible for checking if the supplied user exist in the database and
     * if the credentials are correct.
     *
     * @param loginUser a {@link LoginUser} object passed as json
     * @throws  InvalidCredentialsException if the credentials are
     * invalid, without any additional specificity.
     * @return {@link AuthMsg} with JWT token on success
     * @see ServerException
     */
    @PostMapping("/api/login")
    public AuthMsg login(@RequestBody LoginUser loginUser) {
        LOG.info("Token requested for: '{}'", loginUser.getUsrName());
        try {
            String token = this.userService.signIn(loginUser.getUsrName(), loginUser.getUsrPassword());
            return new AuthMsg(token);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }
    }

    @GetMapping("/api/reset/{email}")
    public GenericMsg getResetEmail(@PathVariable String email) throws MessagingException {
        User user = userRepository.findByUsrEmailEquals(email);
        if(user != null) {
            String reset_code = randomService.generate_random_ints(9);
            emailSender.sendingMail(user.getUsrEmail(), "Reset password",
                    "Your code is: " + reset_code + ". This will be valid for 10 minutes");
            user.setUsrResetCode(reset_code);
            user.setUsrResetCodeAt(OffsetDateTime.now());
            userRepository.save(user);
        } else {
            LOG.info("User with email <{}> not found", email);
        }
        return new GenericMsg("An email with the instructions to reset your password will be " +
                "sent to the registered email if it existe.");
    }

    @PostMapping("/api/reset/{code}")
    public GenericMsg resetWithCode(@PathVariable String code, @RequestBody PasswordResetUser passwordObj) {
        LOG.info("Password: {}", passwordObj.getUsrPassword());
        LOG.info("Code: {}", code);
        User user = userRepository.findUserByUsrResetCodeEquals(code);
        String password = passwordObj.getUsrPassword();
        if(user == null) throw new InvalidCodeException("Reset code is invalid");
        if(ChronoUnit.SECONDS.between(OffsetDateTime.now(), user.getUsrResetCodeAt()) > 60 * 10) {
            throw new InvalidCodeException("Reset code has expired");
        }
        if(password == null) throw new BadRequestException();
        user.setUsrPasswordHash(password);
        return new GenericMsg("User has been successfully reset");
    }

    /**
     * Accesses {@link Authentication} to get the username
     * to call {@link AuthService#refresh(String username)}
     * which will create a new token with a fresh expiry date.
     * 
     * @param authentication an {@link Authentication} object passed automatically
     * @throws NotFoundException when user associated with the token doesn't exist.
     * @return {@link AuthMsg} with new JWT token on success
     * @see AuthService#refresh(String)
     */
    @GetMapping("/api/token/refresh")
    public AuthMsg refreshToken(Authentication authentication) {
        LOG.info("Refresh requested for: '{}'", authentication.getName());
        return new AuthMsg(this.authService.refresh(authentication.getName()));
    }
}