package com.google.sps.data;

/** Individual user data. */
public final class User {
  private final boolean isLoggedin;
  private final String email;
  private final String url;
  
/**
* User's information constructor.
* 
* @param  isLoggedin  boolean value whether user is logged in or not.
* @param  email the user's email address.
* @param  url the logged-in or logged-out URL links.
*/
  public User(boolean isLoggedin, String email, String url) {
    this.isLoggedin = isLoggedin;
    this.email = email;
    this.url = url;
  }
}
