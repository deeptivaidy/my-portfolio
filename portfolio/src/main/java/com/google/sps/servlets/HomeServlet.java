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
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* A Servlet that takes logged-in users to the comments page and redirects 
* users who are not logged in to the login page
*/
@WebServlet("/login")
public class HomeServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson(); 
        response.setContentType("application/json");
        UserService userService = UserServiceFactory.getUserService();
        
        //The first field is the logged-in user's email, the second field is the 
        //URL to login/logout depending on current status
        String[] loginStatus = new String[2];

        
        if (userService.isUserLoggedIn()) {
            String userEmail = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = "/";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
            loginStatus[0] = userEmail;
            loginStatus[1] = logoutUrl;

        } else {
            String urlToRedirectToAfterUserLogsIn = "/comment.html";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            loginStatus[0] = "";
            loginStatus[1] = loginUrl;
        }
        String json = gson.toJson(loginStatus);
        response.getWriter().println(json);

    }


}