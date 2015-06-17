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
import oracle.kv.FaultException;
import oracle.kv.Version;

public class SingleDataUpdate extends HttpServlet {

    public void updateData(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {

        PrintWriter pw = response.getWriter();
        try {
            final String majorKeyString = request.getParameter("majorKeyString");
            final String minorKeyString = request.getParameter("minorKeyString");
            final String keyValue = request.getParameter("keyValue");
            Version putIfPresent = kvstore.putIfPresent(Key.createKey(majorKeyString, minorKeyString), Value.createValue(keyValue.getBytes()));
            long version = putIfPresent.getVersion();
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Values Inserted</title>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("Values Updated:");
            pw.println("<table border='dashed'><tr>");
            pw.println("<td>Major Key Component</td>");

            pw.println("<td>Minor Key Component </td> ");

            pw.println("<td>Value</td>");
            pw.println("<td>Version</td></tr>");
            pw.println("<tr><td>" + majorKeyString + "</td>");
            pw.println("<td>" + minorKeyString + "</td> ");
            pw.println("<td>" + keyValue + "</td>");
             pw.println("<td>" + version + "</td></tr></table>");

            pw.println("</body>");
            pw.println("</html>");

        } catch (Exception e) {
            e.printStackTrace();
            
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<h3>Data Updatation Failed</h3>");
            pw.println("Following may be Reasons:</br>");
            
            pw.println("2. Request May Be Timed Out.</br>");
            pw.println("3. Unknown Exception Has Occured.</br>");
            pw.println("</body>");
            pw.println("</html>");
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
        updateData(request, response, kvstore);

        kvstore.close();
    }
}
