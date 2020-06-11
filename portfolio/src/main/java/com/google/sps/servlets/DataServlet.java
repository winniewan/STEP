package com.google.sps.servlets;

import com.google.sps.data.UfoData;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns UFO Sighting data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}] */
@WebServlet("/ufo-data")
public class DataServlet extends HttpServlet {

  private Collection<UfoData> ufoDatas;

  @Override
  public void init() {
    ufoDatas = new ArrayList<>();

    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/ufo-data.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      double lat = Double.parseDouble(cells[0]);
      double lng = Double.parseDouble(cells[1]);

      ufoDatas.add(new UfoData(lat, lng));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(ufoDatas);
    response.getWriter().println(json);
  }
}
