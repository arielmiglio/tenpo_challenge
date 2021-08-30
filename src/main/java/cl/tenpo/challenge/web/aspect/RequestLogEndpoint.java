package cl.tenpo.challenge.web.aspect;

import cl.tenpo.challenge.service.RequestHistoryService;
import cl.tenpo.challenge.web.model.RequestLogDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Este aspecto es utilizado para persistir datos de cada request que se hace en la API
 * @author Ariel Miglio
 * @date 25/8/2021
 */

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class RequestLogEndpoint {

    @Autowired
    RequestHistoryService requestHistoryService;

    @Around("execution(* cl.tenpo.challenge.web.controller.*.*(..))")
    public Object log(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object value;


        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
        RequestLogDTO requestLogDTO = RequestLogDTO.builder()
                .method(request.getMethod())
                .requestURI(request.getRequestURI())
                .remoteAddr(request.getRemoteAddr()).build();

        try {
            value = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            requestLogDTO.setError(throwable.getMessage());
            throw throwable;
        }finally {
            requestHistoryService.saveRequest(requestLogDTO);
        }
        return value;
    }

}
