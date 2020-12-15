// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.sps.data.Comment;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  private static final long serialVersionUID = 5770012060147035495L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    int requestLimit = Integer.parseInt(request.getParameter("request-limit"));
    String language = request.getParameter("language");

    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);

    List<Comment> comments = new ArrayList<>();
    for (Entity entity : results.asIterable(FetchOptions.Builder.withLimit(requestLimit))) {
      String author = (String) entity.getProperty("author");
      String text = (String) entity.getProperty("text");

      Translate translate = TranslateOptions.newBuilder().setProjectId("internplayground")
          .setQuotaProjectId("internplayground").build().getService();
      Translation translation = translate.translate(text,
          Translate.TranslateOption.targetLanguage(language));
      text = translation.getTranslatedText();
      Comment comment = new Comment(author, text);
      comments.add(comment);
    }

    Gson gson = new Gson();
    String jsonComments = gson.toJson(comments);
    response.setContentType("application/json; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(jsonComments);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentText = request.getParameter("comment-text");
    String commentAuthor = request.getParameter("comment-author");

    // do not store comments with no contents
    if (commentText.equals("")) {
      return;
    }

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("author", commentAuthor);
    commentEntity.setProperty("text", commentText);
    commentEntity.setProperty("timestamp", System.currentTimeMillis());

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/comments.html");
  }
}
