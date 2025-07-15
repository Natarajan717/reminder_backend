package event.reminder.reminder.repository;

import event.reminder.reminder.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmailAndPassword(String username, String password); // for login

    Optional<AppUser> findByEmail(String email);

    @Query("SELECT u FROM AppUser u WHERE u.email = :username")
    Optional<AppUser> findByEmail1(@Param("username") String username);

}
