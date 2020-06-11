package com.google.sps.servlets;

import com.google.sps.data.Data;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns Covid-19 data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}] */
@WebServlet("/covid-data")
public class DataServlet extends HttpServlet {

  private Collection<Data> covidDatas;

  @Override
  public void init() {
    covidDatas = new ArrayList<>();

    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/ufo-data.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      double lat = Double.parseDouble(cells[0]);
      double lng = Double.parseDouble(cells[1]);

      covidDatas.add(new Data(lat, lng));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(covidDatas);
    response.getWriter().println(json);
  }
}
