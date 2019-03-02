package dominos.demo.controller;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.BaseException;
import dominos.demo.util.exceptions.InvalidLogInException;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static final String LOGGED = "logged";

    public static boolean isLoggedIn(HttpSession session) {
        if(session.isNew() || session.getAttribute(LOGGED) == null) {
            return false;
        }
        return true;
    }

    public static void logUser(HttpSession session, User user) {
        session.setAttribute(LOGGED, user);
    }

    protected static void validateLoginAdmin(HttpSession session)throws BaseException {
        if(!isLoggedIn(session)){
            throw new InvalidLogInException("You are not logged!");
        }
        else {
            User user = (User)(session.getAttribute(LOGGED));
            if(!user.isAdmin()) {
                throw new InvalidLogInException("You are not admin!");
            }
        }
    }
}
