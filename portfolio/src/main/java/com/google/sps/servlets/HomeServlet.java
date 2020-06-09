package com.google.sps.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class HomeServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String index = "/index.html";
    User userData;
    if (userService.isUserLoggedIn()) {
      String userEmail = userService.getCurrentUser().getEmail();
      String logoutUrl = userService.createLogoutURL(index);
      
      userData = new User(true, userEmail, logoutUrl);
    } else {
      String loginUrl = userService.createLoginURL(index);
      
      userData = new User(false, null, loginUrl);
    }

    response.setContentType("application/json;");
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(userData));
  }
}
