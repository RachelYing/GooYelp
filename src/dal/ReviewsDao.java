package dal;


import model.Restaurants;
import model.Reviews;

import java.sql.*;

public class ReviewsDao {
    private ConnectionManager connectionManager;
    private RestaurantsDao restaurantsDao;
    private static ReviewsDao reviewsDao;

    private ReviewsDao() {
        this.connectionManager = new ConnectionManager();
        this.restaurantsDao = RestaurantsDao.getInstance();
    }

    public static ReviewsDao getInstance() {
        if (reviewsDao == null) {
            reviewsDao = new ReviewsDao();
        }
        return reviewsDao;
    }

    public Reviews create(Reviews reviews) throws SQLException {
        String sqlTemplate = "INSERT INTO Reviews(RestaurantId,Value,Content) VALUES(?,?,?);";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, reviews.getRestaurant().getRestaurantId());
            preparedStatement.setInt(2, reviews.getValue());
            preparedStatement.setString(3, reviews.getContent());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                reviews.setReviewId(resultSet.getInt(1));
            }
            return reviews;
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

    public Reviews getByReviewId(Integer reviewId) throws SQLException {
        String sqlTemplate = "SELECT * FROM Reviews WHERE ReviewId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Reviews reviews = null;
        Restaurants restaurant = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, reviewId);
            ResultSet resultSet = preparedStatement.executeQuery();
            restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
            if (resultSet.next()) {
                reviews = new Reviews(
                        resultSet.getInt("ReviewId"),
                        restaurant,
                        resultSet.getInt("Value"),
                        resultSet.getString("Content")
                );
            }
            return reviews;
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

    public Reviews updateReviewValue(Reviews reviews, Integer newValue) throws SQLException {
        String sqlTemplate = "UPDATE Reviews SET Value = ? WHERE ReviewId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, newValue);
            preparedStatement.setInt(2, reviews.getReviewId());
            preparedStatement.executeUpdate();
            reviews.setValue(newValue);
            return reviews;
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

    public Reviews updateReviewContent(Reviews reviews, String newContent) throws SQLException {
        String sqlTemplate = "UPDATE Reviews SET Content = ? WHERE ReviewId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setString(1, newContent);
            preparedStatement.setInt(2, reviews.getReviewId());
            preparedStatement.executeUpdate();
            reviews.setContent(newContent);
            return reviews;
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

    public Reviews delete(Reviews reviews) throws SQLException {
        String sqlTemplate = "DELETE * FROM Reviews WHERE ReviewId = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionManager.getConnection();
            preparedStatement = connection.prepareStatement(sqlTemplate);
            preparedStatement.setInt(1, reviews.getReviewId());
            preparedStatement.executeUpdate();
            return reviews;
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
