package cl.tenpo.challenge.model;

import cl.tenpo.challenge.web.model.RequestLogDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Ariel Miglio
 * @date 25/8/2021
 */

@Entity
@Table(name = "request_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String method;
    @Column
    private String requestURI;
    @Column
    private String remoteAddr;
    @Column(length = 1000)
    private String error;
    @Column
    private Date executionDateTime;

    public static RequestLog createFromDTO(RequestLogDTO requestLogDTO){
        RequestLog requestLog = RequestLog.builder()
                .method(requestLogDTO.getMethod())
                .requestURI(requestLogDTO.getRequestURI())
                .remoteAddr(requestLogDTO.getRemoteAddr())
                .executionDateTime(new Date())
                .error(requestLogDTO.getError()).build();

        return requestLog;
    }
}
