package controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import util.exceptions.BaseException;
import util.exceptions.ErrorMsg;
import util.exceptions.InvalidLogInException;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


public abstract class BaseController {

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
    protected void validateLogin(HttpSession session) throws InvalidLogInException {
        if(session.getAttribute("loggedUser") == null){
            throw new InvalidLogInException("You are not logged! Please log in to continue!");
        }
    }

}
