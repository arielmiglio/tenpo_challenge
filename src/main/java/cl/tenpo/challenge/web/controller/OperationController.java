package cl.tenpo.challenge.web.controller;

import cl.tenpo.challenge.service.OperationService;
import cl.tenpo.challenge.web.model.OperationResultDTO;
import cl.tenpo.challenge.web.model.OperatorsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * @author Ariel Miglio
 * @date 20/8/2021
 */

@RestController()
@RequestMapping("/api/v1/operation")
@Slf4j
public class OperationController {

    @Autowired
    private OperationService operationService;

    @Operation(summary = "Suma los números contenidos en el objeto recibido")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Ocurrió un error al realizar la operación"),
                            @ApiResponse(responseCode = "401", description = "Error de autenticación - Debe haber un usuario logueado")})

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/plus")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)//si la operación se comienza a persistir, se debe retornar HttpStatus.CREATED
    public OperationResultDTO plus(@Valid @RequestBody OperatorsDTO operatorsDTO){
        log.debug("Ejecutando suma" + operatorsDTO.getOperator1() + " + " + operatorsDTO.getOperator2());
        BigDecimal result = operationService.plusNumbers(operatorsDTO);
        return new OperationResultDTO(result);
    }
}
