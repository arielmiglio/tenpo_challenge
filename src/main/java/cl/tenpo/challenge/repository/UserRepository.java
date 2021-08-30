package cl.tenpo.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.tenpo.challenge.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario con el nombre de usuario pasado como parámetro
     * @param username
     * @return true cuando encuentra un usuario con el nombre buscado
     */
    boolean existsByUsername(String username);

    /**
     * Busca un usuario con el nombre pasado como parámetro
     * @param username
     * @return un Optional. En caso de encontrar el usuario, lo retorna.
     */
    Optional<User> findByUsername(String username);



}
