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

import com.google.gson.Gson;
import com.google.sps.data.Comment;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

    private static final long serialVersionUID = 5770012060147035495L;


    private ArrayList<Comment> comments = new ArrayList<Comment>();

    @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    String jsonComments = gson.toJson(comments);
    response.setContentType("application/json;");
    response.getWriter().println(jsonComments);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String commentText = request.getParameter("comment-text");
    String commentAuthor = request.getParameter("comment-author");

    //do not store comments with no contents
    if (commentText.equals("")) {
      return;
    }
    Comment comment = new Comment(commentAuthor,commentText);
    comments.add(comment);
    response.sendRedirect("/comments.html");
  }
}
