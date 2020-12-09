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
import java.util.Arrays;
import java.util.Date;

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

    private ArrayList<String> messages = new ArrayList<String>(
      Arrays.asList("message 1","message 2","message 3"));

    @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Gson gson = new Gson();
    String jsonMessages = gson.toJson(messages);
    response.setContentType("application/json;");
    response.getWriter().println(jsonMessages);
  }


  //private final Date startTime = new Date();

  /*
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Calculate server stats
    Date currentTime = new Date();
    long maxMemory = Runtime.getRuntime().maxMemory();
    long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

    // Convert the server stats to JSON
    ServerStats serverStats = new ServerStats(startTime, currentTime, maxMemory, usedMemory);
    String json = convertToJson(serverStats);

    // Send the JSON as the response
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
*/

// not needed yet
//   private String convertCommentToJson(Comment comment) {
//     Gson gson = new Gson();
//     String json = gson.toJson(comment);
//     return json;
//   }
}
