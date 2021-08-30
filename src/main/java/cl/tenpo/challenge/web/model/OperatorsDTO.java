package cl.tenpo.challenge.web.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */
@Data
@AllArgsConstructor
@Builder
public class OperatorsDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "El campo operator1 es obligatorio")
    private BigDecimal operator1;


    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @NotNull(message = "El campo operator2 es obligatorio")
    private BigDecimal operator2;


}
