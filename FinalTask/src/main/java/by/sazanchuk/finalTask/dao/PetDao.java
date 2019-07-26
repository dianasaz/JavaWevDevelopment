package by.sazanchuk.finalTask.dao;

import by.sazanchuk.finalTask.entity.Pet;
import by.sazanchuk.finalTask.entity.PetList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.attribute.UserPrincipal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PetDao extends BaseDao implements Dao<Pet> {
    private static final String INSERT_ALL_INFO = "INSERT INTO `mydatabase`.pet (`name`, `kind`, `date_of_birth`, `user_id`) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_INFO = "SELECT `name`, `kind`, `date_of_birth`, `user_id` FROM `mydatabase`.pet WHERE `id` = ?";
    private static final String UPDATE_PET = "UPDATE `mydatabase`.pet SET `name` = ?, `date_of_birth` = ?, `kind` = ?, `user_id` = ? WHERE `id` = ?";
    private static final String DELETE_FROM_DATABASE = "DELETE FROM `mydatabase`.pet WHERE `id` = ?";
    private static final String SELECT_ALL_PETS_WITH_ONE_USER = "SELECT `id` FROM `mydatabase`.pet WHERE `user_id` = ?";
    private static final String READ_ALL_INFORMATION_ABOUT_PET = "SELECT `user_id`, `date_of_birth`, `kind`,`name`, `id` FROM `mydatabase`.pet ORDER BY `name`";
    private static final String SELECT_PET_NAME_USER_ID = "SELECT `id` FROM `mydatabase`.pet WHERE `name` = ? AND `user_id` = ?";
    private static final String INSERT_ALL_INFO_IN_PET_USER = "INSERT INTO `mydatabase`.user_pet (`pet_id`, `user_id`) VALUES (?, ?)";
    private static final String DELETE_FROM_PET_USER = "DELETE FROM `mydatabase`.user_pet WHERE `pet_id` = ?";

    private final Logger log = LogManager.getLogger(PetDao.class);

    @Override
    public Integer create(Pet entity) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(INSERT_ALL_INFO, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getKind().toString());
          //  statement.setObject(3, entity.getDateOfBirth());
            if (entity.getDateOfBirth() != null) {
                statement.setDate(3, new Date(entity.getDateOfBirth().getTime()));
            } else {
                statement.setDate(3, new Date(new java.util.Date().getTime()));
            }
            statement.setInt(4, entity.getUser_identity());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                log.error("There is no autoincremented index after trying to add record into table `users`");
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch(SQLException e) {}
            try {
                if (statement != null) statement.close();
            } catch(SQLException e) {}
        }
    }

    public void createPetUser(Pet entity) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(INSERT_ALL_INFO_IN_PET_USER, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, entity.getUser_identity());
            statement.setInt(2, entity.getIdentity());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch(SQLException e) {}
        }
    }

    @Override
    public Pet read(Integer id) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_INFO);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            Pet pet = null;
            if (resultSet.next()) {
                pet = new Pet();
                pet.setIdentity(id);
                pet.setDateOfBirth(resultSet.getDate("date_of_birth"));
                pet.setKind(PetList.setPet(resultSet.getString("kind")));
                pet.setName(resultSet.getString("name"));
                pet.setUser_identity(resultSet.getInt("user_id"));
                //pet.setEventList(null);
            }
            return pet;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch(SQLException e) {}
            try {
                if (statement != null) statement.close();
            } catch(SQLException e) {}
        }
    }

    @Override
    public void update(Pet entity) throws DaoException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_PET)) {
            statement.setString(1, entity.getName());
            statement.setObject(2, entity.getDateOfBirth());
            statement.setString(3, entity.getKind().toString());
            statement.setInt(4, entity.getUser_identity());
            statement.setInt(5, entity.getIdentity());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(Integer id) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(DELETE_FROM_DATABASE);
            statement.setInt(1, id);
            statement.executeUpdate();
           // deletePetUser(id);
        } catch(SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (statement != null) statement.close();
            } catch(SQLException e) {}
        }
    }

    public void deletePetUser(Integer id) throws DaoException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(DELETE_FROM_PET_USER);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch(SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (statement != null) statement.close();
            } catch(SQLException e) {}
        }
    }


    public List<Pet> readPetsWithOneUser(int user_id) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_ALL_PETS_WITH_ONE_USER);
            statement.setInt(1, user_id);
            resultSet = statement.executeQuery();
            List<Pet> pets = new ArrayList<>();
            Pet pet = null;
            while (resultSet.next()) {
                pet = new Pet();
                pet.setIdentity(resultSet.getInt("id"));
                pets.add(pet);
            }
            return pets;
        } catch(SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch(SQLException e) {}
            try {
                if (statement != null) statement.close();
            } catch(SQLException e) {}
        }
    }

    public List<Pet> read() throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(READ_ALL_INFORMATION_ABOUT_PET );
            resultSet = statement.executeQuery();
            List<Pet> pets = new ArrayList<>();
            Pet pet = null;
            while(resultSet.next()) {
                pet = new Pet();
                pet.setIdentity(resultSet.getInt("id"));
                pet.setDateOfBirth((Date) resultSet.getObject("date_of_birth"));
                pet.setKind(PetList.getById(resultSet.getInt("kind")));
                pet.setName(resultSet.getString("name"));
                pet.setUser_identity(resultSet.getInt("user_id"));
                pets.add(pet);
            }
            return pets;
        } catch(SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
            } catch(SQLException e) {}
            try {
                if (statement != null)
                    statement.close();
            } catch(SQLException e) {}
        }
    }

    public Pet read(String name, Integer user_id) throws DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(SELECT_PET_NAME_USER_ID);
            statement.setString(1, name);
            statement.setInt(2, user_id);
            resultSet = statement.executeQuery();
            Pet pet = null;
            if (resultSet.next()) {
                pet = new Pet();
                pet.setIdentity(resultSet.getInt("id"));
                pet.setName(name);
                pet.setUser_identity(user_id);
            }
            return pet;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
            } catch(SQLException e) {}
            try {
                if (statement != null) statement.close();
            } catch(SQLException e) {}
        }
    }







}
