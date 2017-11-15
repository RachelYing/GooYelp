package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dal.CuisineDao;


@WebServlet("/cuisine")
public class CuisineServlet extends HttpServlet {

  protected CuisineDao cuisineDao;

  @Override
  public void init() throws ServletException {

  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

  }


  @Override
  public void doPut(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

  }


  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

  }

}
