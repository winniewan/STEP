package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for listing comments. */
@WebServlet("/comment")
public class CommentServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Obtain # of comments to display from URL parameter input.
    String pageSizeString = request.getParameter("page-size");
    int pageSize = getNumber(pageSizeString);

    // Sort entities according to time stamp.
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);;

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Retreive entities based on page size.
    List<Entity> entities = results.asList(FetchOptions.Builder.withLimit(pageSize));

    // Convert entities to Comment objects.
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : entities) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty("text");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comment = new Comment(id, text, timestamp);
      comments.add(comment);
    }

    response.setContentType("application/json;");
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter("text-input");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/comments.html");
  }

//   @Override
//   public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
//     long id = Long.parseLong(request.getParameter("id"));

//     Key commentEntityKey = KeyFactory.createKey("Comment", id);
//     DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
//     datastore.delete(commentEntityKey);
//   }
  
  /** Convert String input into an int. */
  private int getNumber(String pageSizeString) {
    try {
      return Integer.parseInt(pageSizeString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + pageSizeString);
      return -1;
    }
  }
}