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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        ArrayList<Comment> messages = new ArrayList<>();

        for(Entity e : results.asIterable()) {
            long id = e.getKey().getId();
            String content = (String) e.getProperty("content");
            String author = (String) e.getProperty("author");
            long timestamp = (long)e.getProperty("timestamp");

            Comment c = new Comment(id, content, author, timestamp);
            messages.add(c);
        }
        
        Gson gson = new Gson();
        String json = gson.toJson(messages);

        response.setContentType("application/json");
        response.getWriter().println(json);
        
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
                UserService userService = UserServiceFactory.getUserService();
        
        String newComment = request.getParameter("comments");
        String author = userService.getCurrentUser().getEmail();
        long timestamp = System.currentTimeMillis();

        Entity  commentEntity = new Entity("Comment");
        commentEntity.setProperty("timestamp", timestamp);
        commentEntity.setProperty("content", newComment);
        commentEntity.setProperty("author", author);

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        datastore.put(commentEntity);
        
        response.sendRedirect("/comment.html");

    }
}
