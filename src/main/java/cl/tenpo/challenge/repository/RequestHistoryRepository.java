package cl.tenpo.challenge.repository;

import cl.tenpo.challenge.model.RequestLog;
import cl.tenpo.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ariel Miglio
 * @date 25/8/2021
 */
@Repository
public interface RequestHistoryRepository extends JpaRepository<RequestLog, Long> {


}
