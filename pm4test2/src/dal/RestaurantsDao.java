package dal;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class RestaurantsDao {
  protected ConnectionManager connectionManager;
  private static RestaurantsDao instance = null;

  protected RestaurantsDao() {
    connectionManager = new ConnectionManager();
  }

  public static RestaurantsDao getInstance() {
    if (instance == null) {
      instance = new RestaurantsDao();
    }
    return instance;
  }

  public Restaurants create(Restaurants restaurant) throws SQLException {
    String insertRestaurant = "INSERT INTO Restaurants(Name,IsOpen,HasDelivery,TakesReservations) "
        + "VALUES(?,?,?,?);";
    Connection connection = null;
    PreparedStatement insertStmt = null;
    ResultSet resultKey = null;

    try {
      connection = connectionManager.getConnection();
      insertStmt = connection.prepareStatement(insertRestaurant, Statement.RETURN_GENERATED_KEYS);
      insertStmt.setString(1, restaurant.getName());
      insertStmt.setBoolean(2, restaurant.getisOpen());
      insertStmt.setBoolean(3, restaurant.isHasDelivery());
      insertStmt.setBoolean(4, restaurant.isTakesReservations());
      insertStmt.executeUpdate();

      resultKey = insertStmt.getGeneratedKeys();
      int restaurantId = -1;
      if (resultKey.next()) {
        restaurantId = resultKey.getInt(1);
      } else {
        throw new SQLException("Unable to retrieve auto-generated key.");
      }
      restaurant.setRestaurantId(restaurantId);
      return restaurant;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (insertStmt != null) {
        insertStmt.close();
      }
    }
  }

  public Restaurants getRestaurantById(int restaurantId) throws SQLException {
    String selectRestaurant = "SELECT * FROM Restaurants WHERE RestaurantId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;
    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setInt(1, restaurantId);
      results = selectStmt.executeQuery();

      if (results.next()) {
        int resultRestaurantId = results.getInt("RestaurantId");
        String name = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(resultRestaurantId, name, isOpen, hasDelivery,
            takesReservations);
        return restaurant;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return null;
  }

  public List<Restaurants> getRestaurantByName(String name) throws SQLException {
    List<Restaurants> restaurants = new ArrayList<Restaurants>();
    String selectRestaurant = "SELECT * FROM Restaurants WHERE RestaurantId = ?;";
    Connection connection = null;
    PreparedStatement selectStmt = null;
    ResultSet results = null;

    try {
      connection = connectionManager.getConnection();
      selectStmt = connection.prepareStatement(selectRestaurant);
      selectStmt.setString(1, name);
      results = selectStmt.executeQuery();

      while (results.next()) {
        int restaurantId = results.getInt("RestaurantId");
        String resultName = results.getString("Name");
        boolean isOpen = results.getBoolean("IsOpen");
        boolean hasDelivery = results.getBoolean("HasDelivery");
        boolean takesReservations = results.getBoolean("TakesReservations");
        Restaurants restaurant = new Restaurants(restaurantId, resultName, isOpen, hasDelivery,
            takesReservations);
        restaurants.add(restaurant);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (selectStmt != null) {
        selectStmt.close();
      }
      if (results != null) {
        results.close();
      }
    }
    return restaurants;
  }

  public Restaurants delete(Restaurants restaurant) throws SQLException {
    String deleteRestaurant = "DELETE FROM Restaurants WHERE RestaurantId = ?;";
    Connection connection = null;
    PreparedStatement deleteStmt = null;

    try {
      connection = connectionManager.getConnection();
      deleteStmt = connection.prepareStatement(deleteRestaurant);
      deleteStmt.setInt(1, restaurant.getRestaurantId());
      deleteStmt.executeUpdate();

      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (connection != null) {
        connection.close();
      }
      if (deleteStmt != null) {
        deleteStmt.close();
      }
    }
  }
}
