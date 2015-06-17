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
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

public class ImportCSV extends HttpServlet {

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public void readCsv(HttpServletRequest request, HttpServletResponse response, KVStore kvstore) throws IOException {
        PrintWriter pw = response.getWriter();
        BufferedReader br = null;
        String line = "\n";
        String splitBy = ",";
        ArrayList<String> keyValue = new ArrayList<String>();
        //String[] KeyValue;

        Integer cnt = 0;

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String majorKeyComponent = request.getParameter("majorKeyComponent");

        HttpSession session = request.getSession(true);
        String fileName = (String) session.getAttribute("fileName");
        //pw.println(fileName);
        //pw.println(majorKeyComponent);
        String minorKeyComponent = request.getParameter("minorKeyComponent");
        int minorKeyPart = Integer.parseInt(request.getParameter("minorKeyPart"));
        //     pw.println(minorKeyPart);
        String temp = null;
        temp = minorKeyComponent;
        int j = 0;
        try {
            pw.println("<!DOCTYPE html>");
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Values Inserted</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("Imported Data:");
            pw.println("<table border='dashed'>");

            pw.println("<tr><td>Sr.No</td><td>Major Key Component</td><td>Minor Key Component</td><td>Key Value</td></tr>");
            //int m=0;
            int i;
            br = new BufferedReader(new FileReader("E://UploadData/" + fileName));
            String[] data = null;
            //data = new String();
            while ((line = br.readLine()) != null) {
                i = 0;
                keyValue.clear();
                data = line.split(splitBy);

                minorKeyComponent = minorKeyComponent.concat(data[minorKeyPart]);

                Key myKey = Key.createKey(majorKeyComponent, minorKeyComponent);
                
                while (i < data.length) {
                    if (data[i] != (data[minorKeyPart])) {
                        keyValue.add(data[i]);
                    }
                    i++;

                }

                Value myValue = Value.createValue(keyValue.toString().getBytes());

                kvstore.put(myKey, myValue);

                //     pw.println(""+myKey+"");
                //   pw.println(""+myValue+"");
                //   pw.println(""+keyValue+"");
                //  pw.println("value inserted");
                pw.println("<tr><td>" + j + "</td><td>" + majorKeyComponent + "</td><td>" + minorKeyComponent + "</td><td>" + keyValue.toString() + "</td></tr>");
                minorKeyComponent = temp;
                j++;
            }

            pw.println("</table>");
            //m=data.length;
            //pw.println(m);
            pw.println("</body>");
            pw.println("</html>");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //System.out.println("Done with reading CSV");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter pw = response.getWriter();
        //pw.println("a");

        HttpSession session = request.getSession(true);
        String storeName = (String) session.getAttribute("storeName");
        String hostName = (String) session.getAttribute("hostName");
        String hostPort = (String) session.getAttribute("hostPort");

        KVStore kvstore = HelloBigDataWorld.connectStore(storeName, hostName, hostPort);

        readCsv(request, response, kvstore);
        pw.println("Uploaded CSV Imported Successfully!!!");

        kvstore.close();
    }
}
