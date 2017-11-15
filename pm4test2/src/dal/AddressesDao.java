package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Addresses;
import model.Restaurants;

public class AddressesDao {
	protected ConnectionManager connectionManager;
	private static AddressesDao instance = null;

	protected AddressesDao() {
		connectionManager = new ConnectionManager();
	}

	public static AddressesDao getInstance() {
		if (instance == null) {
			instance = new AddressesDao();
		}
		return instance;
	}

	public Addresses create(Addresses address) throws SQLException {
		String insertAddresses = "INSERT INTO Addresses(RestaurantId, FormattedAddress, Latitude, Longtitude, "
				+ "StreetOne, StreetTwo, City, State, Country, ZipCode, Formatted_PhoneNumber,"
				+ "International_PhoneNumber) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement insertStmt = null;
		ResultSet resultKey = null;

		try {
			connection = connectionManager.getConnection();
			insertStmt = connection.prepareStatement(insertAddresses, Statement.RETURN_GENERATED_KEYS);
			insertStmt.setInt(1, address.getRestaurant().getRestaurantId());
			insertStmt.setString(2, address.getFormattedAddress());
			insertStmt.setDouble(3, address.getLatitude());
			insertStmt.setDouble(4, address.getLongitude());
			insertStmt.setString(5, address.getStreetOne());
			insertStmt.setString(6, address.getStreetTwo());
			insertStmt.setString(7, address.getCity());
			insertStmt.setString(8, address.getState());
			insertStmt.setString(9, address.getCountry());
			insertStmt.setString(10, address.getZipCode());
			insertStmt.setString(11, address.getFormattedPhoneNumber());
			insertStmt.setString(12, address.getInternationalPhoneNumber());

			resultKey = insertStmt.getGeneratedKeys();
			int addressId = -1;
			if (resultKey.next()) {
				addressId = resultKey.getInt(1);
			} else {
				throw new SQLException("Unable to retrieve auto-generated key.");
			}
			address.setAddressId(addressId);
			return address;
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

	public Addresses getAddressById(int addressId) throws SQLException {
		String selectAddress = "SELECT * FROM Address WHERE AddressId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectAddress);
			selectStmt.setInt(1, addressId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();

			if (results.next()) {
				int resultAddressId = results.getInt("AddressId");
				int restaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(restaurantId);
				String formattedAddress = results.getString("FormattedAddress");
				double latitude = results.getDouble("Latitude");
				double longitude = results.getDouble("Longitude");
				String streetOne = results.getString("StreetOne");
				String streetTwo = results.getString("StreetTwo");
				String city = results.getString("City");
				String state = results.getString("State");
				String country = results.getString("Country");
				String zipCode = results.getString("ZipCode");
				String formattedPhoneNumber = results.getString("FormattedPhoneNumber");
				String internationalPhoneNumber = results.getString("InternationalPhoneNumber");
				Addresses address = new Addresses(resultAddressId, restaurant, formattedAddress, latitude, longitude,
						streetOne, streetTwo, city, state, country, zipCode, formattedPhoneNumber,
						internationalPhoneNumber);
				return address;
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

	public List<Addresses> getAddressesByRestaurantId(int restaurantId) throws SQLException {
		List<Addresses> list = new ArrayList<Addresses>();
		String selectAddress = "SELECT * FROM Address WHERE RestaurantId = ?;";
		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet results = null;

		try {
			connection = connectionManager.getConnection();
			selectStmt = connection.prepareStatement(selectAddress);
			selectStmt.setInt(1, restaurantId);
			results = selectStmt.executeQuery();
			RestaurantsDao restaurantDao = RestaurantsDao.getInstance();

			while (results.next()) {
				int addressId = results.getInt("AddressId");
				int resultRestaurantId = results.getInt("RestaurantId");
				Restaurants restaurant = restaurantDao.getRestaurantById(resultRestaurantId);
				String formattedAddress = results.getString("FormattedAddress");
				double latitude = results.getDouble("Latitude");
				double longitude = results.getDouble("Longitude");
				String streetOne = results.getString("StreetOne");
				String streetTwo = results.getString("StreetTwo");
				String city = results.getString("City");
				String state = results.getString("State");
				String country = results.getString("Country");
				String zipCode = results.getString("ZipCode");
				String formattedPhoneNumber = results.getString("FormattedPhoneNumber");
				String internationalPhoneNumber = results.getString("InternationalPhoneNumber");
				Addresses address = new Addresses(addressId, restaurant, formattedAddress, latitude, longitude,
						streetOne, streetTwo, city, state, country, zipCode, formattedPhoneNumber,
						internationalPhoneNumber);
				list.add(address);
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
		return list;
	}

	public Addresses delete(Addresses address) throws SQLException {
		String deleteAddresses = "DELETE FROM Addresses WHERE AddressId=?;";
		Connection connection = null;
		PreparedStatement deleteStmt = null;
		try {
			connection = connectionManager.getConnection();
			deleteStmt = connection.prepareStatement(deleteAddresses);
			deleteStmt.setInt(1, address.getAddressId());
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
