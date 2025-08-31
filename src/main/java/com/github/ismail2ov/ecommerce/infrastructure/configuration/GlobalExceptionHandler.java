package com.github.ismail2ov.ecommerce.infrastructure.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.github.ismail2ov.ecommerce.domain.exception.BasketNotFoundException;
import com.github.ismail2ov.ecommerce.domain.exception.CrossSellRelationAlreadyExistsException;
import com.github.ismail2ov.ecommerce.domain.exception.ProductNotFoundException;
import com.github.ismail2ov.ecommerce.infrastructure.controller.model.ErrorResponseRDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ProductNotFoundException.class, BasketNotFoundException.class})
    public ResponseEntity<ErrorResponseRDTO> handleNotFoundException(Exception e) {
        return this.buildResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(CrossSellRelationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseRDTO> handleConflict(Exception e) {
        return this.buildResponse(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({InternalServerError.class})
    public ResponseEntity<ErrorResponseRDTO> handleInternalServerException(Exception ex) {
        return this.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponseRDTO> buildResponse(HttpStatus httpStatus, Exception e) {
        ErrorResponseRDTO errorResponseRDTO = new ErrorResponseRDTO()
            .message(e.getMessage())
            .type(httpStatus.getReasonPhrase())
            .code(httpStatus.value());

        return new ResponseEntity<>(errorResponseRDTO, httpStatus);
    }

}
