package com.google.sps.data;

/** Class representing a comment */
public class Comment {

  private final String author;
  private final String text;

  public Comment(String author, String text) {
    this.author = author;
    this.text = text;
  }

  public String getText() {
    return text;
  }

  public String getAuthor() {
        return author;
    }

}