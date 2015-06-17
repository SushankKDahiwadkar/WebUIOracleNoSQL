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

public class MultiDataUpdate extends HttpServlet {

    public void updateMultiData(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {

        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession(true);

        try {

            final String majorKeyComponent = (String) session.getAttribute("majorKeyComponent");
            final int minorKeyCount = Integer.parseInt((String) session.getAttribute("minorKeyCount"));

            String minorKeyComponent[] = new String[100];
            String minorKeyValues[] = new String[100];
            int i;
            String temp1 = null;
            String temp2 = null;
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Values Updated</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("Values Updated:");
            pw.println("<table border='dashed'>");
            pw.println("<tr><td>Sr.No</td><td>Major Key Component</td><td>Minor Key Component</td><td>Key Value</td></tr>");
            for (i = 0; i < minorKeyCount; i++) {

                temp1 = "minorKeyComponent" + String.valueOf(i);
                temp2 = "keyValue" + String.valueOf(i);
                minorKeyComponent[i] = request.getParameter(temp1);
                minorKeyValues[i] = request.getParameter(temp2);
                kvstore.putIfPresent(Key.createKey(majorKeyComponent, minorKeyComponent[i]), Value.createValue(minorKeyValues[i].getBytes()));

                pw.println("<tr><td>" + i + "</td><td>" + majorKeyComponent + "</td><td>" + minorKeyComponent[i] + "</td><td>" + minorKeyValues[i] + "</td></tr>");

            }
            pw.println("</table>");
            pw.println("</body>");
            pw.println("</html>");
        } catch (RuntimeException e) {
            e.printStackTrace();
            
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<h3>Data Updation Failed</h3>");
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
        updateMultiData(request, response, kvstore);

        kvstore.close();
    }
}
