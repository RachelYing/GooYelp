package servlet;

import dal.*;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/findusers")
public class FindUsers extends HttpServlet {
  protected UsersDao userDao;

  @Override
  public void init() throws ServletException {
    userDao = UsersDao.getInstance();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    List<Users> users = new ArrayList<Users>();

    // Retrieve and validate name.
    // firstname is retrieved from the URL query string.
    String firstName = req.getParameter("firstname");
    if (firstName == null || firstName.trim().isEmpty()) {
      messages.put("success", "Please enter a valid name.");
    } else {
      // Retrieve users, and store as a message.
      try {
        users = userDao.getUsersFromFirstName(firstName);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying results for " + firstName);
      // Save the previous search term, so it can be used as the default
      // in the input box when rendering FindUsers.jsp.
      messages.put("previousFirstName", firstName);
    }
    req.setAttribute("users", users);

    req.getRequestDispatcher("/FindUsers.jsp").forward(req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // Map for storing messages.
    Map<String, String> messages = new HashMap<String, String>();
    req.setAttribute("messages", messages);

    List<Users> users = new ArrayList<Users>();

    // Retrieve and validate name.
    // firstname is retrieved from the form POST submission. By default, it
    // is populated by the URL query string (in FindUsers.jsp).
    String firstName = req.getParameter("firstname");
    if (firstName == null || firstName.trim().isEmpty()) {
      messages.put("success", "Please enter a valid name.");
    } else {
      // Retrieve users, and store as a message.
      try {
        users = userDao.getUsersFromFirstName(firstName);
      } catch (SQLException e) {
        e.printStackTrace();
        throw new IOException(e);
      }
      messages.put("success", "Displaying results for " + firstName);
    }
    req.setAttribute("users", users);

    req.getRequestDispatcher("/FindUsers.jsp").forward(req, resp);
  }
}
