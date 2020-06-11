package com.google.sps.data;

/** Represents COVID-19 affected area at a specific lat lng point. */
public class Data {
  private double lat;
  private double lng;

  public Data(double lat, double lng) {
    this.lat = lat;
    this.lng = lng;
  }
}
