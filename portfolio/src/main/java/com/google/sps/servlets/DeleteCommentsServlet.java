package com.google.sps.servlets;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

/** Servlet that deletes all comments from datastore */
@WebServlet("/delete-data")
public class DeleteCommentsServlet extends HttpServlet {

  private static final long serialVersionUID = -1685141346361232578L;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Iterator<Entity> results = datastore.prepare(query).asIterator();

    while (results.hasNext()) {
      Entity comment = results.next();
      Key key = comment.getKey();
      datastore.delete(key);
    }

    response.sendRedirect("/comments.html");
  }
}