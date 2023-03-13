package servlets;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import services.*;
import java.util.logging.*;
import javax.servlet.RequestDispatcher;
import models.*;

public class UserServlet extends HttpServlet {

   @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    UserService us = new UserService();
    RoleService rs = new RoleService();
    String action = request.getParameter("action");

    if (action != null) {
        if (action.equals("edit")) {
            try {
                String email = request.getParameter("email");
                String role_name = request.getParameter("role");
                User user = us.get(email);
                List<User> users = us.getAll();
                request.setAttribute("users", users);
                request.setAttribute("email", email);
                request.setAttribute("selectedUser", user);
                request.setAttribute("message", "edit");
                
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action.equals("delete")) {
            try {
                String email = request.getParameter("email");
                us.delete(email);
                request.setAttribute("message", "delete");
                response.sendRedirect("/");
                return;
            } catch (Exception ex) {
                Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } else {
        try {
            List<User> users = us.getAll();
            request.setAttribute("users", users);
            if (users.isEmpty()) {
                request.setAttribute("message", "empty");
            }
        } catch (Exception ex) {
            Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
            request.setAttribute("message", "error");
        }
    }
    getServletContext().getRequestDispatcher("/WEB-INF/users.jsp").forward(request, response);

}


    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    UserService us = new UserService();
    RoleService rs = new RoleService();

    String email = request.getParameter("email");
String first = request.getParameter("first");
String last = request.getParameter("last");
String pw = request.getParameter("pw");
String id = request.getParameter("role");

String action = request.getParameter("action");

if (email == null || email.isEmpty() || first == null || first.isEmpty() ||
        last == null || last.isEmpty() || pw == null || pw.isEmpty()) {
    request.setAttribute("mes", "All fields are required");
} else {
    try {
        List<User> users = us.getAll();
        request.setAttribute("users", users);

        if (action != null) {
            int user_role_id = (id.equals("1") || id.equals("system admin")) ? 1 : 2;
            switch (action) {
                case "add":
                    if (users.stream().anyMatch(u -> u.getEmail().equals(email))) {
                        request.setAttribute("mes", "Error. Email is already taken");
                    } else {
                        us.insert(email, first, last, pw, rs.get(user_role_id));
                        request.setAttribute("message", "add");
                    }
                    break;

                case "update":
                    us.update(email, first, last, pw, rs.get(user_role_id));
                    request.setAttribute("message", "update");
                    break;
            }
        }
    } catch (Exception ex) {
        Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
        request.setAttribute("message", "error");
    }
}

       try {
           request.setAttribute("users", us.getAll());
       } catch (Exception ex) {
           Logger.getLogger(UserServlet.class.getName()).log(Level.SEVERE, null, ex);
       }
if (request.getAttribute("message") == null) {
    request.setAttribute("message", "empty");
}
if (request.getAttribute("mes") == null) {
    request.setAttribute("mes", "");
}

RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/users.jsp");
dispatcher.forward(request, response);

}
}