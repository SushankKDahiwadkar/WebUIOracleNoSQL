package HelloBigDataWorld;

import java.io.*;
import java.io.EOFException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
import oracle.kv.FaultException;

import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.Key;
import oracle.kv.Value;
import oracle.kv.ValueVersion;

public class HelloBigDataWorld extends HttpServlet {

    public static KVStore connectStore(String storeName, String hostName, String hostPort) throws IOException, ServletException {
        //response.setContentType("text/html;charset=UTF-8");
        //PrintWriter pw = response.getWriter();
        //String storeName1 = storeName;//"kvstore";//request.getParameter("storeName");
        //String hostName1 = hostName;//"localhost";//request.getParameter("hostName");
        //String hostPort1 = hostPort;//"5000";//request.getParameter("hostPort");

        KVStoreConfig kconfig = new KVStoreConfig(storeName, hostName + ":" + hostPort);
        KVStore kvstore = KVStoreFactory.getStore(kconfig);
        /*
         try {
          
         pw.println("<!DOCTYPE html>");
         pw.println("<html>");
         pw.println("<head>");
         pw.println("<title>HelloBigDataWorld</title>");            
         pw.println("</head>");
         pw.println("<body>");
         pw.println("<h1>Welcome to KVSTORE</h1>");
         pw.println("</body>");
         pw.println("</html>");
         } finally {
         pw.close();
         }
         */
        //pw.println("Welcome To KVSTORE");
        return kvstore;
    }

    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter pw = response.getWriter();
        String storeName = request.getParameter("storeName");
        String hostName = request.getParameter("hostName");
        String hostPort = request.getParameter("hostPort");
        KVStore kvstore = connectStore(storeName, hostName, hostPort);
        //insertData(request,response,kvstore);
        //displayData(request,response,kvstore);

        HttpSession session = request.getSession();
        session.setAttribute("storeName", storeName);
        session.setAttribute("hostName", hostName);
        session.setAttribute("hostPort", hostPort);
        pw.println("<!DOCTYPE html>");
        pw.println("<html>");
        pw.println("<head>");
        pw.println("<title>HelloBigDataWorld</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<h1>Welcome to KVSTORE</h1>");

        pw.println("</body>");
        pw.println("</html>");

        kvstore.close();
    }
}
