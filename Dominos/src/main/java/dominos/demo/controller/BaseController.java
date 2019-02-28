package dominos.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.ErrorMsg;

import java.time.LocalDateTime;

public class BaseController {

    //@ExceptionHandler({NotLoggedInException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleNotLogged(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        return msg;
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg handleBaseErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        return msg;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMsg handleOtherErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        return msg;
    }

}
