package cl.tenpo.challenge.exception;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This class represents the main exception handler for the controller layer.
 * All the non caught runtime exceptions thrown will be handled here and provide a proper HTTP response
 */
@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler{



    @ExceptionHandler(value = UsernameAlreadyExistException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleBookingNotFound(UsernameAlreadyExistException e) {
        return ErrorResponse.builder().
                message("Username: " + e.getUsername() + " ya se encuentra en uso").
                build();
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleAlreadyCancelledBooking(UserNotFoundException e) {
        return ErrorResponse.builder().
                message("El nombre de usuario : " + e.getUsername()+ " no existe").
                build();
    }


    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e, WebRequest request) {
        String error = e.getName() + " debería ser del tipo " + e.getRequiredType().getName();
        return ErrorResponse.builder().
        		message(error).
                build();
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return ErrorResponse.builder().
                message("Operación no autorizada. Aseguresé de haber ingresado al sistema").
                build();
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse handleBadCredentialsException(BadCredentialsException e) {
        return ErrorResponse.builder().
                message("El usuario o contraseña son incorrectos").
                build();
    }

    /**
     * Manejo de excepciones no controladas anteriormente para mantener el formato
     *
     * @param ex      Exception
     * @param request
     * @return ResponseEntity with HTTP status 500
     */
    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleMethodException(Exception ex, WebRequest request) {
        
        ErrorResponse response = ErrorResponse.builder().message(ex.getMessage()).build();
        return response;
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        ErrorResponse errorResponse = new ErrorResponse("Falló una validación de parámetros", errors);

        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {

        String error = ex.getParameterName() + " parámetros erróneos";
        List<String> errors = new LinkedList<String>();
        errors.add(error);
        ErrorResponse errorResponse = new ErrorResponse("Falló una validación de parámetros", errors);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    
}
