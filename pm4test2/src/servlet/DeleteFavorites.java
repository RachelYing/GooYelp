package servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dal.*;
import model.*;


@WebServlet("/deletefavorites")
public class DeleteFavorites extends HttpServlet {
	protected FavoritesDao favoritesDao;

	@Override
	public void init() throws ServletException {
		favoritesDao = FavoritesDao.getInstance();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Map for storing messages.
		Map<String, String> messages = new HashMap<String, String>();
		req.setAttribute("messages", messages);
		// Provide a title and render the JSP.
		messages.put("title", "Delete Favorites");
		req.getRequestDispatcher("/DeleteFavorites.jsp").forward(req, resp);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Map for storing messages.
		Map<String, String> messages = new HashMap<String, String>();
		req.setAttribute("messages", messages);

		// Retrieve and validate name.
		String favoriteId = req.getParameter("favoriteId");
		if (favoriteId == null || favoriteId.trim().isEmpty()) {
			messages.put("title", "Invalid UserName");
			messages.put("disableSubmit", "true");
		} else {
			Favorites favorite;
			try {
				favorite = favoritesDao.getFavoriteById(Integer.parseInt(favoriteId));
				favorite = favoritesDao.delete(favorite);
				if (favorite == null) {
					messages.put("title", "Successfully deleted " + favorite.getFavoriteId());
					messages.put("disableSubmit", "true");
				} else {
					messages.put("title", "Failed to delete " + favorite.getFavoriteId());
					messages.put("disableSubmit", "false");
				}
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		}

		req.getRequestDispatcher("/DeleteFavorites.jsp").forward(req, resp);
	}
}
