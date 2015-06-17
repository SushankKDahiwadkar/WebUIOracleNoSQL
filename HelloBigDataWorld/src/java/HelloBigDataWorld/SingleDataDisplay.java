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
import oracle.kv.ValueVersion;
import HelloBigDataWorld.HelloBigDataWorld;
import javax.servlet.http.HttpSession;

public class SingleDataDisplay extends HttpServlet {

    public void displayData(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {

        PrintWriter pw = response.getWriter();
        try {
            final String majorKeyString = request.getParameter("majorKeyString");
            final String minorKeyString = request.getParameter("minorKeyString");

            ValueVersion vv = kvstore.get(Key.createKey(majorKeyString, minorKeyString));
            Value v = vv.getValue();
            String data = new String(v.getValue());
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>HelloBigDataWorld</title>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("Result Generated:");
            pw.println("<table border='dashed'><tr>");
            pw.println("<td>Major Key Component</td>");

            pw.println("<td>Minor Key Component </td> ");

            pw.println("<td>Value</td></tr>");
            pw.println("<tr><td>" + majorKeyString + "</td>");
            pw.println("<td>" + minorKeyString + "</td> ");
            pw.println("<td>" + data + "</td></tr></table>");

            pw.println("</body>");
            pw.println("</html>");

        } catch (RuntimeException e) {
            e.printStackTrace();
            pw.println("error");
        }

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(true);
        String storeName = (String) session.getAttribute("storeName");
        String hostName = (String) session.getAttribute("hostName");
        String hostPort = (String) session.getAttribute("hostPort");

        KVStore kvstore = HelloBigDataWorld.connectStore(storeName, hostName, hostPort);

        displayData(request, response, kvstore);

        kvstore.close();
    }
}
