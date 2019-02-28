package dominos.demo.controller;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.InvalidLogInException;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static final String LOGGED = "logged";

    public static boolean isLoggedIn(HttpSession session) throws InvalidLogInException {
        if(session.isNew() || session.getAttribute(LOGGED) == null) {
            return false;
        }
        return true;
    }

    public static void logUser(HttpSession session, User user) {
        session.setAttribute(LOGGED, user);
    }

}
