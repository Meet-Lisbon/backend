package pt.meetlisbon.backend.controllers;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pt.meetlisbon.backend.exceptions.*;
import pt.meetlisbon.backend.models.entities.User;
import pt.meetlisbon.backend.models.entities.UserRole;
import pt.meetlisbon.backend.models.repository.UserRepository;
import pt.meetlisbon.backend.models.requests.CreateUser;
import pt.meetlisbon.backend.models.requests.PatchUser;
import pt.meetlisbon.backend.models.requests.GenericMsg;
import pt.meetlisbon.backend.services.EmailSender;
import pt.meetlisbon.backend.services.RandomService;
import pt.meetlisbon.backend.services.UserService;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

/**
 * This controller is responsible for managing the /api/user endpoint,
 * responding to requests for the creation, update, deletion and fetching
 * of user objects and related entities.
 * It makes use of {@link UserRepository} which bridges the gap between database and server
 * It also calls functions from {@link UserService} for specific user behaviour.
 */
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    final UserRepository userRepository;
    private final UserService userService;
    private final EmailSender emailSender;
    private final RandomService randomService;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @GetMapping("/me")
    public User getMe(Authentication authentication) {
        User myUser = userRepository.findUserByUsrNameEquals(authentication.getName());
        if (myUser == null) throw new NotFoundException("User");
        return myUser;
    }

    /**
     * Gets user from email or username
     * If no params are present, returns all users
     * @param email a {@link String} object passed as request parameter
     * @param username a {@link String} object passed as request parameter
     * @throws NotFoundException if user was not found
     * @return {@link Iterable} of type {@link User} object on success
     * @see org.hibernate.service.spi.ServiceException
     */
    @GetMapping
    public Iterable<User> getUsers(@RequestParam(required = false) String email,
                        @RequestParam(required = false) String username) {
        User user = null;
        if(email != null) user = userRepository.findByUsrEmailEquals(email);
        else if(username != null) user = userRepository.findUserByUsrNameEquals(username);
        if(user == null) {
            return userRepository.findAll();
        }
        return Collections.singleton(user);
    }


    /**
     * Registers a user in the database
     *
     * @param createUser a {@link CreateUser} object passed as json
     * @throws AlreadyExistsException if the user already exists
     * @see UserRepository
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createUser(@RequestBody CreateUser createUser) {
        LOG.info("User: {}", createUser);
        LOG.info("First Name: {}", createUser.getFirstName());
        LOG.info("Last Name: {}", createUser.getLastName());
        LOG.info("Email: {}", createUser.getUsrEmail());
        LOG.info("Password: {}", createUser.getUsrPassword());
        if(userRepository.existsByUsrName(createUser.getUsrName())) {
            throw new AlreadyExistsException("User");
        }
        try {
            String usr_verification = randomService.generate_random_ints(9);

            User user = new User();
            user.setId(UUID.randomUUID());
            user.setUsrFirstName(createUser.getFirstName());
            user.setUsrLastName(createUser.getLastName());
            user.setUsrEmail(createUser.getUsrEmail());
            user.setUsrName(createUser.getUsrName());
            user.setUsrPasswordHash(createUser.getUsrPassword());
            user.setUsrRole(UserRole.user);
            user.setUsrActive(true);
            user.setUsrEmailVerified(false);
            user.setUsrRegisterCode(usr_verification);
            user.setUsrResetCodeAt(OffsetDateTime.now());
            user.setCreatedAt(OffsetDateTime.now());
            user.setUpdatedAt(OffsetDateTime.now());
            emailSender.sendingMail(createUser.getUsrEmail(), "Welcome to MeetLisbon", "Your activation code is: " + usr_verification);

            userRepository.save(user);

        } catch (Exception e) {
            LOG.info(e.getMessage());
            throw new BadRequestException();
        }
    }

    // todo comments
    @GetMapping("/verify/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public GenericMsg verifyUser(@PathVariable String code) {
        User user = userRepository.findUserByUsrRegisterCodeEquals(code);
        if(user == null) throw new InvalidCodeException("Registration code is invalid");
        user.setUsrEmailVerified(true);
        user.setUsrRegisterCode(null);
        userRepository.save(user);
        return new GenericMsg("User is verified");
    }

    /**
     * Updates users based on partial mapping of a user object
     *
     * @param newUser a {@link PatchUser} object passed as json body
     * @throws NotFoundException if the user being modified wasn't found
     * @see UserRepository
     */
    @PatchMapping("/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@RequestBody PatchUser newUser, Authentication authentication) {
        String name = authentication.getName();

        LOG.info("Authentication: " + name);
        User user = userRepository.findUserByUsrNameEquals(name);
        boolean newUserNameExists = userRepository.existsByUsrName(newUser.getUsrName()) && !newUser.getUsrName().equals(user.getUsrName());
        boolean newEmailExists = userRepository.existsByUsrEmail(newUser.getUsrEmail()) && !newUser.getUsrEmail().equals(user.getUsrEmail());
        if(newUserNameExists && newEmailExists) {
            throw new AlreadyExistsException("Email & Username");
        }
        if(newUserNameExists) throw new AlreadyExistsException("Username");
        if(newEmailExists) throw new AlreadyExistsException("Email");
        if (user == null) {
            throw new NotFoundException("User");
        }
        userService.partialUpdate(newUser, user);

    }

    /**
     * Deletes a user based on email and username attributes
     * At least one of the parameters needs to be present
     * @param email a {@link String} object passed as a request parameter
     * @param username a {@link String} object passed as a request parameter
     * @throws NotFoundException if the user being deleted wasn't found
     * @see UserRepository
     */
    @DeleteMapping
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestParam(required = false) String email,
            @RequestParam(required = false) String username, Authentication authentication) {
        User myUser = userRepository.findUserByUsrNameEquals(authentication.getName());
        LOG.info("Role: {}", myUser.getUsrRole());
        if(myUser.getUsrRole() == UserRole.user) throw new NotAuthorizedException();
        LOG.info("Request params => email: {}, username: {}", email, username);
        User user = null;
        if (email != null) {
            user = userRepository.findByUsrEmailEquals(email);
        } else if (username != null) {
            user = userRepository.findUserByUsrNameEquals(username);
        }
        if(user == null) throw new NotFoundException("User");
        userRepository.delete(user);
    }

}