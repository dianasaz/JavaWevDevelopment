package by.sazanchuk.finalTask.command.action.profileUser;

import by.sazanchuk.finalTask.command.action.Command;
import by.sazanchuk.finalTask.command.action.CommandResult;
import by.sazanchuk.finalTask.dao.DaoException;
import by.sazanchuk.finalTask.service.ServiceException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EventsOfOnePetCommand implements Command {
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, DaoException {
        return null;
    }
}
