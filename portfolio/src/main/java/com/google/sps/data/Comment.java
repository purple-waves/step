package com.google.sps.data;

import java.util.Date;

/** Class containing server statistics. */
public final class Comment {

  private final String author;
  private final String text;
  private final Date postTime;

  public Comment(String author, String text, Date postTime) {
    this.author = author;
    this.text = text;
    this.postTime = postTime;
  }

  public String getAuthor() {
    return author;
  }

  public String getText() {
    return text;
  }

  public Date getPostTime() {
    return postTime;
  }
}