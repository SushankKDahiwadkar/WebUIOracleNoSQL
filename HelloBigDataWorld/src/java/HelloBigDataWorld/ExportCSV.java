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
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpSession;
import oracle.kv.Direction;
import oracle.kv.KeyValueVersion;

public class ExportCSV extends HttpServlet {
    Date date = new Date();
    String csvFileToWrite = "D:"+ File.separator + date.getTime()+ ".csv";//"D:\\outpu.csv";
    
    public void exportDataToCSV(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException, ServletException {
        HttpSession session = request.getSession();
        session.setAttribute("currentExport", csvFileToWrite);
        FileWriter writer = new FileWriter(csvFileToWrite, true);
        PrintWriter pw = response.getWriter();
        String keyString = null;
        String minorKeyVal=null;

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        //System.out.println("Enter DataSet Name-");
        keyString = request.getParameter("majorKeyString");
        //System.out.println("Enter Table Name-");
        //String tableName = input.readLine();
        //dataSet = dataSet.concat(tableName);
        //keyString = dataSet;

        Key myKey = Key.createKey(keyString);
        Iterator<KeyValueVersion> i = kvstore.multiGetIterator(Direction.FORWARD, 0, myKey, null, null);
        Iterator<KeyValueVersion> iMinorKey = kvstore.multiGetIterator(Direction.FORWARD, 0, myKey, null, null);
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
            String data = new String(v.getValue());
            pw.println("<tr><td>" + j + "</td><td>" + myKey + "</td><td>"+ minorKeyVal +"</td><td>" + data + "</td></tr>");
            j++;
            writer.append("\n");

            writer.append( myKey.toString() + ",");
            writer.append(minorKeyVal + ",");
            
            writer.append(data);
        }
        pw.println("</table>");

        pw.println("<a href=\"/HelloBigDataWorld/DownloadFile\">Download Generated file</a>");
        pw.println("</body>");
        pw.println("</html>");
        kvstore.close();

        writer.close();

    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter pw = response.getWriter();
        //pw.println("a");

        HttpSession session = request.getSession(true);
        String storeName = (String) session.getAttribute("storeName");
        String hostName = (String) session.getAttribute("hostName");
        String hostPort = (String) session.getAttribute("hostPort");
        //pw.println(storeName);
        //pw.println(hostName);
        //pw.println("hostPort");
        KVStore kvstore = HelloBigDataWorld.connectStore(storeName, hostName, hostPort);
        //insertData(request,response,kvstore);
        exportDataToCSV(request, response, kvstore);
        //response.sendRedirect("http://localhost:8080/HelloBigDataWorld/Start.html");
        kvstore.close();
    }
}
