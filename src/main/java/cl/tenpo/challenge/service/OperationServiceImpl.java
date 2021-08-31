package cl.tenpo.challenge.service;

import cl.tenpo.challenge.web.model.OperatorsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */
@Service
public class OperationServiceImpl implements OperationService{
    @Value("${bigdecimal.scale}")
    private Integer bigDecimalScale;

    public static RoundingMode roundingMode = RoundingMode.HALF_UP;

    @Override
    public BigDecimal sumNumbers(OperatorsDTO operatorsDTO) {
        return operatorsDTO.getOperator1().add(operatorsDTO.getOperator2()).setScale(bigDecimalScale, roundingMode);
    }
}
