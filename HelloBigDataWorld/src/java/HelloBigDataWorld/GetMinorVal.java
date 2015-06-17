package HelloBigDataWorld;

import java.io.*;
import java.io.EOFException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import oracle.kv.FaultException;

import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.Key;
import oracle.kv.Value;
import oracle.kv.ValueVersion;
import HelloBigDataWorld.HelloBigDataWorld;
import javax.servlet.http.HttpSession;

public class GetMinorVal extends HttpServlet {

    public void getData(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        int count = Integer.parseInt(request.getParameter("minorKeyCount"));
        int i = 0;
        //pw.println(count);

        pw.println("<!DOCTYPE html PUBLIC \'-//W3C//DTD XHTML 1.0 Transitional//EN\' \'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\'>");
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<title>HelloBigDataWorld</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<h1>Enter Your Records.</h1>");
        pw.println("<form action = '/HelloBigDataWorld/MultiDataInsert' method= GET >");
        pw.println("<table>");
        while (i < count) {
            pw.println("<tr><td>Enter Minor Component : <input type='text' name='minorKeyComponent" + i + "'></td><td>Enter Value: <input type='text' name='keyValue" + i + "'></td></tr>");
            i++;
        }
        pw.println("</table>");

        pw.println("<input type='submit' value= 'GO'>");
        pw.println("</form>");

        pw.println("</body>");
        pw.println("</html>");

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter pw = response.getWriter();
        //pw.println("a");

        //HttpSession session = request.getSession(true);
        HttpSession session = request.getSession();

        String majorKeyComponent = request.getParameter("majorKeyComponent");
        String minorKeyCount = request.getParameter("minorKeyCount");
        session.setAttribute("majorKeyComponent", majorKeyComponent);
        session.setAttribute("minorKeyCount", minorKeyCount);
        getData(request, response);
        //pw.println(majorKeyComponent);
        //pw.println(minorKeyCount);

        //session.setAttribute("hostPort", hostPort);
        //String majorKeyComponent = (String) session.getAttribute("majorKeyComponent");
        //int count = Integer.parseInt( request.getParameter("minorKeyCount"));
        //String hostPort = (String) session.getAttribute("hostPort");
        //pw.println(storeName);
        //pw.println(hostName);
        //pw.println("hostPort");
        //KVStore kvstore = HelloBigDataWorld.connectStore(storeName, hostName, hostPort);
        //insertData(request,response,kvstore);
        //displayData(request,response,kvstore);
        //response.sendRedirect("http://localhost:8080/HelloBigDataWorld/Start.html");
        //kvstore.close();
        pw.close();
    }
}
