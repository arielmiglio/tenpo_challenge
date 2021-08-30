package cl.tenpo.challenge.service;

import cl.tenpo.challenge.web.model.RequestLogDTO;
import cl.tenpo.challenge.web.model.RequestsPagedListDTO;

/**
 * Servicio que registra los request y provee acceso a los mismos de forma paginada
 * @author Ariel Miglio
 * @date 25/8/2021
 */
public interface RequestHistoryService {

    /**
     * Crea un nuevo requestLog y lo persiste
     * @param requestLogDTO
     */
    void saveRequest(RequestLogDTO requestLogDTO);

    /**
     * Obtiene la historia de request que se hicieron a la API de la base de datos y la retorna paginada
     *
     * @param pageNumber puede ser nulo, en dicho caso se retorna la página 0 (cero)
     * @param pageSize puede ser nulo, en dicho caso se retorna lo configurado en application.properties
     * @return DTO con los registros obtenidos
     */
    RequestsPagedListDTO getRequestHistory(Integer pageNumber, Integer pageSize);
}
