package cl.tenpo.challenge.service;

import cl.tenpo.challenge.model.RequestLog;
import cl.tenpo.challenge.repository.RequestHistoryRepository;
import cl.tenpo.challenge.web.model.RequestLogDTO;
import cl.tenpo.challenge.web.model.RequestsPagedListDTO;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ariel Miglio
 * @date 25/8/2021
 */
@Service
@Setter
public class RequestHistoryServiceImpl implements RequestHistoryService{

    @Autowired
    RequestHistoryRepository requestHistoryRepository;
    private static Integer DEFAULT_PAGE_NUMBER = 0;
    @Value("${default.page.size}")
    private Integer defaultPageSize;


    @Override
    public void saveRequest(RequestLogDTO requestLogDTO) {
        RequestLog requestLog = RequestLog.createFromDTO(requestLogDTO);
        this.requestHistoryRepository.save(requestLog);
    }

    @Override
    public RequestsPagedListDTO getRequestHistory(Integer pageNumber, Integer pageSize) {
        pageNumber = Optional.ofNullable(pageNumber).orElse(DEFAULT_PAGE_NUMBER);
        pageSize = Optional.ofNullable(pageSize).orElse(this.defaultPageSize);

        Pageable pageable = PageRequest.of(pageNumber,
                Integer.valueOf(pageSize),
                Sort.by("executionDateTime").descending());

        Page<RequestLog> requestLogPage = requestHistoryRepository.findAll(pageable);
        List<RequestLogDTO> requestLogDTOList = requestLogPage.stream().map(RequestLogDTO::requestLogDTOFromRequestLog).collect(Collectors.toList());


        return new RequestsPagedListDTO(
                requestLogDTOList
                , PageRequest.of(
                requestLogPage.getPageable().getPageNumber(),
                requestLogPage.getPageable().getPageSize()),
                requestLogPage.getTotalElements());
    }
}
