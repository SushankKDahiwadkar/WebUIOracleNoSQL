package HelloBigDataWorld;

import java.io.*;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.kv.KVStore;
import oracle.kv.Key;
import HelloBigDataWorld.HelloBigDataWorld;
import javax.servlet.http.HttpSession;

public class MultiDataDelete extends HttpServlet {

    public void deleteMultipleData(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {

        PrintWriter pw = response.getWriter();
        try {
            final String majorKeyString = request.getParameter("majorKeyComponent");
            
            

            kvstore.multiDelete(Key.createKey(majorKeyString),null,null);

            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Values Deleted</title>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("Values Deleted With Following Parameters:");
            pw.println("<table border='dashed'><tr>");
            pw.println("<td>Major Key Component</td>");

            

            
            pw.println("<tr><td>" + majorKeyString + "</td>");
            
            

            pw.println("</body>");
            pw.println("</html>");

        } catch (RuntimeException e) {
            e.printStackTrace();
            //pw.println("error");
        }
    }

    /**
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(true);
        String storeName = (String) session.getAttribute("storeName");
        String hostName = (String) session.getAttribute("hostName");
        String hostPort = (String) session.getAttribute("hostPort");

        KVStore kvstore = HelloBigDataWorld.connectStore(storeName, hostName, hostPort);
        deleteMultipleData(request, response, kvstore);

        kvstore.close();
    }
}
