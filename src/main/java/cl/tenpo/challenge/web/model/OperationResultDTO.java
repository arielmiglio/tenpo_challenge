package cl.tenpo.challenge.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@Data
@AllArgsConstructor
public class OperationResultDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal result;

}
