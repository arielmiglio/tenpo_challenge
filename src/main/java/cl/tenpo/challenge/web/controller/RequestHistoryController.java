package cl.tenpo.challenge.web.controller;

import cl.tenpo.challenge.service.RequestHistoryService;
import cl.tenpo.challenge.web.model.RequestsPagedListDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Ariel Miglio
 * @date 25/8/2021
 */

@RestController()
@RequestMapping("/api/v1/requests")
@Slf4j
public class RequestHistoryController {


    @Autowired
    RequestHistoryService requestHistoryService;

    private static final Integer PAGE_NUMBER_DEFAULT = 0;

    @Operation(summary = "Obtiene el historial de llamados http")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Ocurrió un error al realizar la operación"),
            @ApiResponse(responseCode = "401", description = "Error de autenticación - Debe haber un usuario logueado")})

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/hist")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public RequestsPagedListDTO list(@RequestParam(value = "pageNumber", required = false) Integer pageNumber, @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        log.debug("Invocación al API que retorna historia de requests");
        return requestHistoryService.getRequestHistory(pageNumber, pageSize);
    }
}
