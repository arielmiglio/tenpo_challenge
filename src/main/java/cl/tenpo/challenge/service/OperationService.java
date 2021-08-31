package cl.tenpo.challenge.service;

import cl.tenpo.challenge.web.model.OperatorsDTO;

import java.math.BigDecimal;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */


public interface OperationService {

    /**
     * Suma los números contenidos en el DTO recibido como parámetro
     * @param operatorsDTO DTO con operandos a sumar
     * @return BigDecimal con el resultado de la operación
     */
    BigDecimal sumNumbers(OperatorsDTO operatorsDTO);

}
