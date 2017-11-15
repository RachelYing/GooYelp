package dal;

import model.FoodSafety;
import model.Restaurants;

import java.sql.*;

public class FoodSafetyDao {
    private ConnectionManager connectionManager;
    private RestaurantsDao restaurantsDao;
    private static FoodSafetyDao foodSafetyDao;

    private FoodSafetyDao() {
        this.connectionManager = new ConnectionManager();
        this.restaurantsDao = RestaurantsDao.getInstance();
    }

    public static FoodSafetyDao getInstance() {
        if (foodSafetyDao == null) {
            foodSafetyDao = new FoodSafetyDao();
        }
        return foodSafetyDao;
    }

    public FoodSafety create(FoodSafety foodSafety) throws SQLException {
        String sqlTemplate = "INSERT INTO FoodSafety(RestaurantId,InspectionScore,InspectionResult,InspectionDate) VALUES(?,?,?,?);";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, foodSafety.getRestaurant().getRestaurantId());
            preparedStatement.setInt(2, foodSafety.getInspectionScore());
            preparedStatement.setString(3, foodSafety.getInspectionResult());
            preparedStatement.setDate(4, (Date) foodSafety.getInspectionDate());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                foodSafety.setFoodSafetyId(resultSet.getInt(1));
            }
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public FoodSafety getByFoodSafetyId(Integer foodSafetyId) throws SQLException {
        String sqlTemplate = "SELECT * FROM FoodSafety WHERE FoodSafetyId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        FoodSafety foodSafety = null;
        Restaurants restaurant = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, foodSafetyId);
            ResultSet resultSet = preparedStatement.executeQuery();
            restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
            if (resultSet.next()) {
                foodSafety = new FoodSafety(
                        resultSet.getInt("FoodSafetyId"),
                        restaurant,
                        resultSet.getInt("InspectionScore"),
                        resultSet.getString("InspectionResult"),
                        resultSet.getDate("InspectionDate")
                );
            }
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public FoodSafety updateInspectionScore(FoodSafety foodSafety, Integer newScore, String newResult, java.util.Date newDate) throws SQLException {
        String sqlTemplate = "UPDATE FoodSafety SET InspectionScore = ?, InspectionResult = ?, InspectionDate = ? WHERE FoodSafetyId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, newScore);
            preparedStatement.setString(2, newResult);
            preparedStatement.setDate(3, (Date) newDate);
            preparedStatement.setInt(4, foodSafety.getFoodSafetyId());
            preparedStatement.executeUpdate();
            foodSafety.setInspectionScore(newScore);
            foodSafety.setInspectionResult(newResult);
            foodSafety.setInspectionDate(newDate);
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    public FoodSafety delete(FoodSafety foodSafety) throws SQLException {
        String sqlTemplate = "DELETE * FROM FoodSafety WHERE FoodSafetyId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, foodSafety.getFoodSafetyId());
            preparedStatement.executeUpdate();
            return foodSafety;
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

}
