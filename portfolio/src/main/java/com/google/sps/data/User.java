package com.google.sps.data;

/** Individual user data. */
public final class User {
  private final boolean isLoggedin;
  private final String email;
  private final String url;

  public User(boolean isLoggedin, String email, String url) {
    this.isLoggedin = isLoggedin;
    this.email = email;
    this.url = url;
  }
}