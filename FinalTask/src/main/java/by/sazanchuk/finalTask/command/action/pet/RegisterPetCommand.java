package by.sazanchuk.finalTask.command.action.pet;

import by.sazanchuk.finalTask.command.ConfigurationManager;
import by.sazanchuk.finalTask.command.action.Command;
import by.sazanchuk.finalTask.command.action.CommandResult;
import by.sazanchuk.finalTask.dao.DaoException;
import by.sazanchuk.finalTask.dao.connectionPool.ConnectionPoolException;
import by.sazanchuk.finalTask.entity.Pet;
import by.sazanchuk.finalTask.entity.PetList;
import by.sazanchuk.finalTask.service.PetService;
import by.sazanchuk.finalTask.service.ServiceException;
import by.sazanchuk.finalTask.service.ServiceFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RegisterPetCommand implements Command {
    private static final Logger logger = LogManager.getLogger(RegisterPetCommand.class);

    private static final String NAME = "name";
    private static final String USER_ID = "user_id";
    private static final String KIND = "kind";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String ERROR_REGISTRATION = "error_registration";
    private static final String ERROR = "error_";

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        String name = request.getParameter(NAME);
        String kind = request.getParameter(KIND);
        String dateOfBirth = request.getParameter(DATE_OF_BIRTH);
       // parameters.put(DATE_OF_BIRTH, request.getParameter(DATE_OF_BIRTH));


        try {
            if (name != null && dateOfBirth != null) {
                createPet(name, kind, dateOfBirth, request);
                //logger.log(Level.INFO, "user registrated and authorized with login - " + parameters.get(LOGIN));
                return new CommandResult("/controller?command=profile_user", false);
            }
            else return goBackWithError(request, "Error");
        } catch (DaoException | ConnectionPoolException e) {
            return goBackWithError(request, "ERROR");
            //throw new ServiceException(e);

        }

    }


    private void createPet(String name, String kind, String dateOfBirth, HttpServletRequest request) throws DaoException, ServiceException, ConnectionPoolException {
        Integer user_id = (Integer) request.getSession().getAttribute(USER_ID);
        if (user_id != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

            ServiceFactory factory = new ServiceFactory();

            PetService service = factory.getService(PetService.class);

            Pet pet = new Pet();

            pet.setName(name);
            pet.setUser_identity(user_id);
            pet.setKind(PetList.setPet(kind));
            try {
                pet.setDateOfBirth(dateFormat.parse(dateOfBirth));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int pet_Id = service.save(pet);
            if (pet_Id != 0) {
                pet.setIdentity(pet_Id);
            } else {
                throw new ServiceException("Can't save pet!");
            }
            setAtributesToSession(pet, request);
        }
    }

    private void setAtributesToSession(Pet pet, HttpServletRequest request) {
       // HttpSession session = request.getSession();
        request.setAttribute("pet", pet);
        request.setAttribute("petName", pet.getName());
    }

    private CommandResult goBackWithError(HttpServletRequest request, String error) {
        request.setAttribute(error, true);
        return new CommandResult(ConfigurationManager.getProperty("path.page.register_pet"), false);
    }
}
