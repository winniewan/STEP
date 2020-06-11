package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet responsible for displaying geomap data. */
@WebServlet("/chart")
public class ChartServlet extends HttpServlet {

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Long> votes = getVotes();

    response.setContentType("application/json;");
    Gson gson = new Gson();
    response.getWriter().println(gson.toJson(votes));
  }

  @Override
  public synchronized void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String region = request.getParameter("region");
    Entity regionEntity = new Entity("Region", region);
    Key entityKey = KeyFactory.createKey("Region", region);

    try {
      regionEntity = datastore.get(entityKey);
      Long preVote = (Long) regionEntity.getProperty("votes");
      regionEntity.setProperty("votes", preVote + 1);
    } catch (Exception e) {
      regionEntity.setProperty("name", region);
      regionEntity.setProperty("votes", 1);
    }

    datastore.put(regionEntity);
    response.sendRedirect("/maps.html");
  }

  private Map<String, Long> getVotes() {
    Query query = new Query("Region");
    PreparedQuery results = datastore.prepare(query);
    
    // Map that stores all the voting data.
    Map<String, Long> votes = new HashMap<String, Long>();
    
    // Parse the entities and store it into map.
    for (Entity entity : results.asIterable()) {
      votes.put((String) entity.getProperty("name"), (Long) entity.getProperty("votes"));
    }

    return votes;
  }
}
