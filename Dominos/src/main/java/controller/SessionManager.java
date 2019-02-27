package controller;
import dominos.demo.model.users.User;
import util.exceptions.InvalidLogInException;

import javax.servlet.http.HttpSession;

public class SessionManager {

    private static final String LOGGED = "logged";

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
