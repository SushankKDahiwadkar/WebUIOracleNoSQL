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
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import oracle.kv.Direction;
import oracle.kv.KeyValueVersion;

public class MultiDataDisplay extends HttpServlet {

    public void displayMultiData(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {

        PrintWriter pw = response.getWriter();

        try {
            final String majorKeyString = request.getParameter("majorKeyString");
            Key myKey = Key.createKey(majorKeyString);
            Iterator<KeyValueVersion> i = kvstore.multiGetIterator(Direction.FORWARD, 0, myKey, null, null);
            Iterator<KeyValueVersion> iMinorKey = kvstore.multiGetIterator(Direction.FORWARD, 0, myKey, null, null);
            String data =null;
            String minorKeyVal=null;
            int j = 1;
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Values Inserted</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("Result Generated:");
            pw.println("<table border='dashed'>");
            pw.println("<tr><td>Sr.No</td><td>Major Key Component</td><td>Minor Key Component</td><td>Key Value</td></tr>");
            while (i.hasNext()) {
                Value v = i.next().getValue();
                minorKeyVal = iMinorKey.next().getKey().getMinorPath().get(0);
                
                data = new String(v.getValue());
                pw.println("<tr><td>" + j + "</td><td>" + myKey + "</td><td>" + minorKeyVal + "</td><td>" + data + "</td></tr>");
                j++;
            }
            pw.println("</table>");
            pw.println("</body>");
            pw.println("</html>");
            kvstore.close();

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

        displayMultiData(request, response, kvstore);
        //response.sendRedirect("http://localhost:8080/HelloBigDataWorld/Start.html");
        kvstore.close();
    }
}
