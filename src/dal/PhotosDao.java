package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import model.Photos;
import model.Restaurants;

public class PhotosDao {

  private ConnectionManager connectionManager;
  private RestaurantsDao restaurantsDao;
  private static PhotosDao photosDao;

  private PhotosDao() {
    this.connectionManager = new ConnectionManager();
    this.restaurantsDao = RestaurantsDao.getInstance();
  }

  public static PhotosDao getInstance() {
    if (photosDao == null) {
      photosDao = new PhotosDao();
    }
    return photosDao;
  }

  public Photos create(Photos photos) throws SQLException {
    String sqlTemplate = "INSERT INTO Photos(RestaurantId,Content) VALUES(?,?);";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
      preparedStatement.setInt(1, photos.getRestaurant().getRestaurantId());
      preparedStatement.setString(2, photos.getContent());
      preparedStatement.executeUpdate();
      ResultSet resultSet = preparedStatement.getGeneratedKeys();
      if (resultSet.next()) {
        photos.setPhotoId(resultSet.getInt(1));
      }
      return photos;
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

  public Photos getByPhotoId(Integer photoId) throws SQLException {
    String sqlTemplate = "SELECT * FROM Photos WHERE PhotoId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Photos photo = null;
    Restaurants restaurant = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, photoId);
      ResultSet resultSet = preparedStatement.executeQuery();
      restaurant = this.restaurantsDao.getRestaurantById(resultSet.getInt("RestaurantId"));
      if (resultSet.next()) {
        photo = new Photos(
            resultSet.getInt("PhotoId"),
            restaurant,
            resultSet.getString("Content")
        );
      }
      return photo;
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

  public Photos updatePhotoLink(Photos photos, String newLink) throws SQLException {
    String sqlTemplate = "UPDATE Photos SET Content = ? WHERE PhotoId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setString(1, newLink);
      preparedStatement.setInt(2, photos.getPhotoId());
      preparedStatement.executeUpdate();
      photos.setContent(newLink);
      return photos;
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

  public Photos delete(Photos photos) throws SQLException {
    String sqlTemplate = "DELETE * FROM Photos WHERE PhotoId = ?";
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try {
      connection = connectionManager.getConnection();
      preparedStatement = connection.prepareStatement(sqlTemplate);
      preparedStatement.setInt(1, photos.getPhotoId());
      preparedStatement.executeUpdate();
      return photos;
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

