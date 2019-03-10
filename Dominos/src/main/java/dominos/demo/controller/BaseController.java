package dominos.demo.controller;


import dominos.demo.util.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;

@RequestMapping(produces = "application/json")
public class BaseController {

    public static Logger logger = Logger.getLogger(BaseController.class.getName());

    @ExceptionHandler({InvalidRegistrationException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleRegisterErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        logger.error(e.getMessage());
        return msg;
    }

    @ExceptionHandler({InvalidLogInException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleLogInErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        logger.error(e.getMessage());
        return msg;
    }

    @ExceptionHandler({InvalidLogOutException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ErrorMsg handleLogOutErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        logger.error(e.getMessage());
        return msg;
    }

    @ExceptionHandler({BaseException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMsg handleBaseErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        logger.error(e.getMessage());
        return msg;
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMsg handleOtherErrors(Exception e) {
        ErrorMsg msg = new ErrorMsg(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
        logger.error(e.getMessage());
        return msg;
    }

}
