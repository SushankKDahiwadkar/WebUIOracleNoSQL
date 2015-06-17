package HelloBigDataWorld;

import java.io.*;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.kv.KVStore;
import oracle.kv.Key;
import oracle.kv.Value;
import HelloBigDataWorld.HelloBigDataWorld;
import javax.servlet.http.HttpSession;

public class SingleDataDelete extends HttpServlet {

    public void deleteSingleData(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {

        PrintWriter pw = response.getWriter();
        try {
            final String majorKeyString = request.getParameter("majorKeyComponent");
            final String minorKeyString = request.getParameter("minorKeyComponent");
            
            
            kvstore.delete(Key.createKey(majorKeyString, minorKeyString));

            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Values Deleted</title>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("Value Deleted With Following Parameters:");
            pw.println("<table border='dashed'><tr>");
            pw.println("<td>Major Key Component</td>");

            pw.println("<td>Minor Key Component </td> ");

            
            pw.println("<tr><td>" + majorKeyString + "</td>");
            pw.println("<td>" + minorKeyString + "</td> ");
            

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
        deleteSingleData(request, response, kvstore);

        kvstore.close();
    }
}
