package com.meir.coupons.exceptions;

import com.meir.coupons.enums.ErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionsHandler {

    //	Response - Object in Spring
    @ExceptionHandler
    @ResponseBody
    // Variable name is throwable in order to remember that it handles Exception and Error
    public ErrorBean toResponse(Throwable throwable, HttpServletResponse response) {

        //	ErrorBean errorBean;
        if (throwable instanceof ApplicationException) {
            ApplicationException appException = (ApplicationException) throwable;

            String errorMessage = appException.getMessage();

            ErrorType errorType = appException.getErrorType();  // in order to get the enum
            /* get 3 parameters from the specific errorType */
            int httpStatus = errorType.getErrorNumber();
            String errorName = errorType.getErrorName();

            response.setStatus(httpStatus);

            ErrorBean errorBean = new ErrorBean(httpStatus, errorMessage, errorName);
            if (appException.getErrorType().isShowStackTrace()) {
                appException.printStackTrace();
            }

            return errorBean;
        }

        response.setStatus(600);

        ErrorBean errorBean = new ErrorBean(601, "General error");
        throwable.printStackTrace();

        return errorBean;
    }

}
