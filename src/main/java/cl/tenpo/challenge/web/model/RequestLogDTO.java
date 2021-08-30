package cl.tenpo.challenge.web.model;

import cl.tenpo.challenge.model.RequestLog;
import lombok.*;

import java.util.Date;

/**
 * @author Ariel Miglio
 * @date 25/8/2021
 */
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestLogDTO {

    private String method;
    private String requestURI;
    private String remoteAddr;
    private String error;
    private Date executionDateTime;

    public static RequestLogDTO requestLogDTOFromRequestLog(RequestLog request){
        return RequestLogDTO.builder()
                .method(request.getMethod())
                .requestURI(request.getRequestURI())
                .remoteAddr(request.getRemoteAddr())
                .error(request.getError())
                .executionDateTime(request.getExecutionDateTime())
                .build();
    }
}
