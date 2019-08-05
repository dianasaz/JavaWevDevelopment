package by.sazanchuk.finalTask.command.action.doctor;

import by.sazanchuk.finalTask.command.ConfigurationManager;
import by.sazanchuk.finalTask.command.action.Command;
import by.sazanchuk.finalTask.command.action.CommandResult;
import by.sazanchuk.finalTask.dao.DaoException;
import by.sazanchuk.finalTask.dao.connectionPool.ConnectionPoolException;
import by.sazanchuk.finalTask.entity.Doctor;
import by.sazanchuk.finalTask.entity.Service;
import by.sazanchuk.finalTask.service.DoctorService;
import by.sazanchuk.finalTask.service.ServiceException;
import by.sazanchuk.finalTask.service.ServiceFactory;
import by.sazanchuk.finalTask.service.ServiceService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddDoctorCommand implements Command {
    private static final Logger logger = LogManager.getLogger(AddDoctorCommand.class);
    private static final String NAME = "name";
    private static final String SERVICES = "service";
    private static final String ERROR_NULL = "error_null";


    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, DaoException {
        String name = request.getParameter(NAME);
        String[] service = request.getParameterValues(SERVICES);

        if (name == null || name.isEmpty() || service.length == 0) {
            logger.log(Level.INFO, "name or price is null");
            return goBackWithError(request, ERROR_NULL);
        } else {
            if (!searchDoctor(name)) {
                List<String> services = Arrays.asList(service);
                Integer a = createDoctor(name, services, request);
                if (a != null) {
                    return new CommandResult("/controller?command=watch_doctor", false); //TODO
                } else return goBackWithError(request, ERROR_NULL);
            } else {
                return goBackWithError(request, ERROR_NULL);
            }
        }
    }


    private CommandResult goBackWithError(HttpServletRequest request, String error) {
        request.setAttribute(error, true);
        return new CommandResult(ConfigurationManager.getProperty("path.page.add_doctor"), false);
    }

    private Integer createDoctor(String name, List<String> services, HttpServletRequest request) throws DaoException {

        ServiceFactory factory = null;
        ServiceService service = null;
        DoctorService doctorService = null;
        try {
            factory = new ServiceFactory();
            service = factory.getService(ServiceService.class);
            doctorService = factory.getService(DoctorService.class);
        } catch (DaoException | ConnectionPoolException e) {
            logger.log(Level.INFO, "service error");
        }


        Doctor d = doctorService.findByName(name);
        if (d == null) {
            d = new Doctor();
            d.setName(name);
            for (String s : services) {
                Service service1 = service.searchServiceByName(s);
                if (service1 != null) {
                    d.addService(service1);
                    doctorService.save(d, service1);
                }
            }
        }

            return d.getIdentity();
        }

        private boolean searchDoctor (String name) throws DaoException {

            ServiceFactory factory = null;
            DoctorService service = null;
            try {
                factory = new ServiceFactory();
                service = factory.getService(DoctorService.class);
            } catch (DaoException | ConnectionPoolException e) {
                logger.log(Level.INFO, "service error");
            }
            assert service != null;
            return service.findByName(name) != null;
        }
    }