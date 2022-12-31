package pt.meetlisbon.backend.models.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pt.meetlisbon.backend.models.entities.User;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsrEmailEquals(String email);
    User findUserByUsrNameEquals(String username);
    User findUserByUsrRegisterCodeEquals(String code);
    User findUserByUsrResetCodeEquals(String code);
    boolean existsByUsrName(String username);
    boolean existsByUsrEmail(String username);
}