package by.sazanchuk.finalTask.service;

import by.sazanchuk.finalTask.dao.DaoException;
import by.sazanchuk.finalTask.dao.UserDao;
import by.sazanchuk.finalTask.dao.connectionPool.ConnectionPoolException;
import by.sazanchuk.finalTask.entity.User;

import java.util.List;

public class UserServiceImpl extends ServiceImpl implements UserService {

    public UserServiceImpl() throws ServiceException {
    }

    @Override
    public List<User> findAll() throws ServiceException {
        UserDao userDao = null;
        try {
            userDao = transaction.createDao(UserDao.class);
            return userDao.read();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User findByIdentity(Integer identity) throws ServiceException {
        UserDao userDao = null;
        try {
            userDao = transaction.createDao(UserDao.class);
            return userDao.read(identity);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public User findByLoginAndPassword(String login, String password) throws ServiceException {
        UserDao userDao = null;
        try {
            userDao = transaction.createDao(UserDao.class);
            User user = null;
            if (login != null && password != null) {
                user = userDao.read(login, PasswordCode.CodeMD5(password));
            }
            return user;
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int save(User user) throws ServiceException {
        try {
            UserDao userDao = transaction.createDao(UserDao.class);
            if (user.getId() != null) {
                user.setPassword(user.getPassword());
                user.setAddress(user.getAddress());
                user.setName(user.getName());
                user.setPhoneNumber(user.getPhoneNumber());
                user.setEmail(user.getEmail());
                userDao.update(user);
            } else {
                user.setPassword(PasswordCode.CodeMD5(user.getPassword()));
                user.setId(userDao.create(user));
            }
            userDao.update(user);
            return user.getId();
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }

    public boolean isExist(String login) throws ServiceException {
        try {
            UserDao userDao = transaction.createDao(UserDao.class);
            return userDao.isExist(login);
        } catch (DaoException e){
            throw new ServiceException(e);
        }

    }

    public boolean searchEmail(String email) throws ServiceException {
        try {
            UserDao userDao = transaction.createDao(UserDao.class);
            return userDao.searchEmail(email);
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }


    @Override
    public void delete(Integer identity) throws ServiceException {
        try {
            UserDao userDao = transaction.createDao(UserDao.class);
            if (findByIdentity(identity) != null) {
                userDao.delete(identity);
            }
        } catch (DaoException e){
            throw new ServiceException(e);
        }
    }
}
