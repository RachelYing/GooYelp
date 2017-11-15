package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dal.*;
import model.*;

@WebServlet("/createfavorites")
public class CreateFavorites extends HttpServlet {
	protected FavoritesDao favoritesDao;

	@Override
	public void init() throws ServletException {
		favoritesDao = FavoritesDao.getInstance();
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> messages = new HashMap<String, String>();
		req.setAttribute("messages", messages);
		req.getRequestDispatcher("/CreateFavorites.jsp").forward(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<String, String> messages = new HashMap<String, String>();
		req.setAttribute("messages", messages);

		String favoriteId = req.getParameter("favoriteId");
		if (favoriteId == null || favoriteId.trim().isEmpty()) {
			messages.put("success", "Invalid favorite id");
		} else {
			String restaurantId = req.getParameter("restaurantId");
			String userId = req.getParameter("userId");
			try {
				Restaurants res = RestaurantsDao.getInstance().getRestaurantById(Integer.parseInt(restaurantId));
				Users user = UsersDao.getInstance().getUserById(Integer.parseInt(userId));
				Favorites favorite = new Favorites(res, user);
				favorite = favoritesDao.create(favorite);
				messages.put("success", "Successfully created favorite: " + " , " + user.getUserId() + res.getName());
			} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}

		req.getRequestDispatcher("/FavoritesCreate.jsp").forward(req, resp);
	}
}
