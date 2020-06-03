package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for listing comments. */
@WebServlet("/list-comments")
public class ListCommentsServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Obtain # of comments to display from URL parameter input.
    String numString = request.getParameter("requestComments");
    int requestComments = getNumber(numString);

    // Sort entities according to time stamp.
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);;

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    // Retreive entities as Comment objects.
    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String text = (String) entity.getProperty("text");
      long timestamp = (long) entity.getProperty("timestamp");

      Comment comment = new Comment(id, text, timestamp);
      comments.add(comment);
    }

    response.setContentType("application/json;");

    // If requested # of comments is greater than total comments, adjust requested # of comments.
    if (comments.size() < requestComments) {
        requestComments = comments.size();
    }

    List<Comment> latestComment = comments.subList(0, requestComments);

    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(latestComment));
  }
  
  /** Convert String input into an int. */
  private int getNumber(String numberString) {
    try {
      return Integer.parseInt(numberString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + numberString);
      return -1;
    }
  }
}
