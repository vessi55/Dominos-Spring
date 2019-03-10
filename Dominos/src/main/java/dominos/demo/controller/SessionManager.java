package dominos.demo.controller;
import dominos.demo.model.users.User;
import dominos.demo.util.exceptions.InvalidLogInException;

import javax.servlet.http.HttpSession;

public class SessionManager {

    public static final String LOGGED = "logged";
    public static final String SHOPPING_CART = "cart";
    public static final String PIZZA = "pizza";
    public static final String PIZZA_INGREDIENTS = "ingredients";


    public static boolean isLoggedIn(HttpSession session) {
        if(session.isNew() || session.getAttribute(LOGGED) == null) {
            return false;
        }
        return true;
    }

    public static void logUser(HttpSession session, User user) {
        session.setAttribute(LOGGED, user);
    }

    protected static boolean validateLoginAdmin(HttpSession session)throws InvalidLogInException {
        if(!isLoggedIn(session)) {
            throw new InvalidLogInException("You are not logged!");
        }
        User user = (User)(session.getAttribute(LOGGED));
        if(!user.isAdmin()) {
            throw new InvalidLogInException("You are not admin!");
        }
        return true;
    }
}
