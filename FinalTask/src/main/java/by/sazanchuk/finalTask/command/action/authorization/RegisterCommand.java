package by.sazanchuk.finalTask.command.action.authorization;

import by.sazanchuk.finalTask.command.ConfigurationManager;
import by.sazanchuk.finalTask.command.action.Command;
import by.sazanchuk.finalTask.command.action.CommandResult;
import by.sazanchuk.finalTask.dao.DaoException;
import by.sazanchuk.finalTask.dao.connectionPool.ConnectionPoolException;
import by.sazanchuk.finalTask.entity.Role;
import by.sazanchuk.finalTask.entity.User;
import by.sazanchuk.finalTask.service.ServiceException;
import by.sazanchuk.finalTask.service.ServiceFactory;
import by.sazanchuk.finalTask.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class RegisterCommand implements Command {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String ERROR_REGISTRATION = "error_registration";
    private static final String ERROR = "error_";
    private static final String ERROR_EMAIL = "error_email";
    private static final String USER_ID = "user_id";
    private static final String USER_ROLE = "user_role";
    private static final String USER = "user";


    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(LOGIN, request.getParameter(LOGIN));
        parameters.put(PASSWORD, request.getParameter(PASSWORD));
        parameters.put(NAME, request.getParameter(NAME));
        parameters.put(ADDRESS, request.getParameter(ADDRESS));
        parameters.put(PHONE_NUMBER, request.getParameter(PHONE_NUMBER));
        parameters.put(EMAIL, request.getParameter(EMAIL));


        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) {
                logger.log(Level.ERROR, "Invalid " + entry.getKey() + " was received");
                return goBackWithError(request, ERROR + entry.getKey());
            }
        }

        boolean userExist;
        boolean emailExist;
        try {
           userExist = checkIfUserExist(parameters.get(LOGIN));
           emailExist = checkIfEmailExist(parameters.get(EMAIL));
        } catch (DaoException | ConnectionPoolException e) {
            throw new ServiceException(e);
        }
        if (userExist) {
            logger.log(Level.INFO, "user with such login already exist");
            return goBackWithError(request, ERROR_REGISTRATION);
        }
        if (emailExist){
            logger.log(Level.INFO, "user with such email already exist");
            return goBackWithError(request, ERROR_EMAIL);
        }
        try {
            createUser(parameters, request);
            logger.log(Level.INFO, "user registrated and authorized with login - " + parameters.get(LOGIN));
            return new CommandResult("/controller?command=home_page", true);
        } catch (DaoException | ConnectionPoolException e) {
            return new CommandResult("/controller?command=login", false);
        }

    }

    private boolean checkIfUserExist(String login) throws DaoException, ConnectionPoolException {

        ServiceFactory factory = new ServiceFactory();

        UserService service = factory.getService(UserService.class);
        return service.isExist(login);
    }

    private boolean checkIfEmailExist(String email) throws DaoException, ConnectionPoolException {

        ServiceFactory factory = new ServiceFactory();

        UserService service = factory.getService(UserService.class);
        return service.searchEmail(email);
    }

    private void createUser(Map<String, String> parameters, HttpServletRequest request) throws DaoException, ServiceException, ConnectionPoolException {
        User user = new User();
        user.setLogin(parameters.get(LOGIN));
        user.setPassword(parameters.get(PASSWORD));
        user.setRole(Role.VISITOR);
        user.setAddress(parameters.get(ADDRESS));
        user.setEmail(parameters.get(EMAIL));
        user.setPhoneNumber(Integer.valueOf(parameters.get(PHONE_NUMBER)));
        user.setName(parameters.get(NAME));


        ServiceFactory factory = new ServiceFactory();
        UserService service = factory.getService(UserService.class);

        service.save(user);
        setAttributesToSession(user, request);

    }

    private void setAttributesToSession(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_ID, user.getId());
        session.setAttribute(USER_ROLE, user.getRole().getName());
        session.setAttribute(USER, user);
        request.setAttribute(USER, true);
    }

    private CommandResult goBackWithError(HttpServletRequest request, String error) {
        request.setAttribute(error, true);
        return new CommandResult(ConfigurationManager.getProperty("path.page.register"), false);
    }
}
