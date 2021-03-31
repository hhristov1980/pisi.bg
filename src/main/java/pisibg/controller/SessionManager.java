package pisibg.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pisibg.exceptions.AuthenticationException;
import pisibg.model.pojo.User;
import pisibg.model.repository.UserRepository;

import javax.servlet.http.HttpSession;

@Getter
@Component
public class SessionManager extends AbstractController{

    private static final String LOGGED_USER_ID = "LOGGED_USER_ID";
    private static final int MAX_INACTIVE_INTERVAL = 10 * 60;

    @Autowired
    private UserRepository repository;

    public User getLoggedUser(HttpSession session) {
        if (session.getAttribute(LOGGED_USER_ID) == null) {
            throw new AuthenticationException("You have to log in!");

        } else {
            int userId = (int) session.getAttribute(LOGGED_USER_ID);
            return repository.findById(userId).get();
        }
    }

    public void loginUser(HttpSession ses, int id) {
        ses.setAttribute(LOGGED_USER_ID, id);
        ses.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
    }


    public void logoutUser(HttpSession ses) {
        ses.invalidate();
    }
}