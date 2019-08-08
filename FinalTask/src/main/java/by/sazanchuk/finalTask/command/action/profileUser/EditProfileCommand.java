package by.sazanchuk.finalTask.command.action.profileUser;

import by.sazanchuk.finalTask.command.ConfigurationManager;
import by.sazanchuk.finalTask.command.action.Command;
import by.sazanchuk.finalTask.command.action.CommandResult;
import by.sazanchuk.finalTask.command.action.authorization.RegisterCommand;
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

public class EditProfileCommand implements Command {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String PHONE_NUMBER = "phoneNumber";
    private static final String EMAIL = "email";
    private static final String ID = "id";
    private static final String ERROR = "error_";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        User olduser = (User) request.getSession().getAttribute("user");

        Map<String, String> oldParam = new HashMap<>();
        oldParam.put(LOGIN, olduser.getLogin());
        oldParam.put(PASSWORD, olduser.getPassword());
        oldParam.put(NAME, olduser.getName());
        oldParam.put(ADDRESS, olduser.getAddress());
        oldParam.put(PHONE_NUMBER, olduser.getPhoneNumber().toString());
        oldParam.put(EMAIL, olduser.getEmail());
        oldParam.put(ID, olduser.getId().toString());

        Map<String, String> parameters = new HashMap<>();
        parameters.put(LOGIN, request.getParameter(LOGIN));
        parameters.put(PASSWORD, request.getParameter(PASSWORD));
        parameters.put(NAME, request.getParameter(NAME));
        parameters.put(ADDRESS, request.getParameter(ADDRESS));
        parameters.put(PHONE_NUMBER, request.getParameter(PHONE_NUMBER));
        parameters.put(EMAIL, request.getParameter(EMAIL));

        if (checkChanges(parameters)){
            try {
                if (checkIfUserExist(parameters.get(LOGIN))) {
                    try {
                        updateUser(parameters, oldParam, request);
                    } catch (DaoException | ConnectionPoolException e) {
                        logger.log(Level.INFO, "dao exception");
                    }
                }
            } catch (DaoException | ConnectionPoolException e) {
                logger.log(Level.INFO, "NO SUCH USER");
            }
            return new CommandResult("controller?command=profile_user", true);
        } else return goBackWithError(request, "error update");

    }

    private boolean checkIfUserExist(String login) throws DaoException, ConnectionPoolException {

        ServiceFactory factory = new ServiceFactory();

        UserService service = factory.getService(UserService.class);
        return service.isExist(login);
    }

    private boolean checkChanges(Map<String, String> param){
        for (Map.Entry<String, String> entry : param.entrySet()) {
            if (entry.getValue() != null) {
                return true;
            }
        }
        return false;
    }

    private void updateUser(Map<String, String> parameters, Map<String, String> oldparam, HttpServletRequest request) throws DaoException, ServiceException, ConnectionPoolException {
        User user = new User();
        if (parameters.get(LOGIN) == null || parameters.get(LOGIN).isEmpty()){
            user.setLogin(oldparam.get(LOGIN));
        } else user.setLogin(oldparam.get(LOGIN));
        if (parameters.get(PASSWORD) == null || parameters.get(PASSWORD).isEmpty()){
            user.setPassword(oldparam.get(PASSWORD));
        } else user.setPassword(oldparam.get(PASSWORD));
        if (parameters.get(EMAIL) == null || parameters.get(EMAIL).isEmpty()){
            user.setEmail(oldparam.get(EMAIL));
        } else user.setEmail(oldparam.get(EMAIL));
        if (parameters.get(ADDRESS) == null || parameters.get(ADDRESS).isEmpty()){
            user.setAddress(oldparam.get(ADDRESS));
        } else user.setLogin(oldparam.get(ADDRESS));
        if (parameters.get(PHONE_NUMBER) == null || parameters.get(PHONE_NUMBER).isEmpty()){
            user.setPhoneNumber(Integer.valueOf(oldparam.get(PHONE_NUMBER)));
        } else user.setPhoneNumber(Integer.valueOf(oldparam.get(PHONE_NUMBER)));
        if (parameters.get(NAME) == null || parameters.get(NAME).isEmpty()){
            user.setName(oldparam.get(NAME));
        } else user.setName(oldparam.get(NAME));
        user.setId(Integer.valueOf(parameters.get(ID)));
        user.setRole(Role.VISITOR);

        ServiceFactory factory = new ServiceFactory();
        UserService service = factory.getService(UserService.class);
        service.save(user);

    }

    private CommandResult goBackWithError(HttpServletRequest request, String error) {
        request.setAttribute(error, true);
        return new CommandResult(ConfigurationManager.getProperty("path.page.edit_profile"), false);
    }
}
