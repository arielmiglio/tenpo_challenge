package cl.tenpo.challenge.web.model;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Ariel Miglio
 * @date 25/8/2021
 */
public class RequestsPagedListDTO extends PageImpl<RequestLogDTO> {

    public RequestsPagedListDTO(List<RequestLogDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

}
