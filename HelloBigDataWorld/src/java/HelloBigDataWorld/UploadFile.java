package HelloBigDataWorld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

@WebServlet(name = "UploadServlet", urlPatterns = {"/upload"})     // specify urlPattern for servlet
@MultipartConfig                                               // specifies servlet takes multipart/form-data
public class UploadFile extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            // get access to file that is uploaded from client
            Part p1 = request.getPart("file");
            InputStream is = p1.getInputStream();

            // read filename which is sent as a part
            //Part p2  = request.getPart("file");
            //Scanner s = new Scanner(p2.getInputStream());
            //final Part filePart = request.getPart("file");
            //final String fileName = getFileName(filePart);
            //String filename = s.nextLine();   // read filename from stream
            HttpSession session = request.getSession();
            String fileName = extractFileName(p1);
            session.setAttribute("fileName", fileName);

            out.println(fileName);
            //String filename = request.getParameter("filename");

            //String filename=p1.getName();
            File file = new File("E://UploadData/" + fileName);
            //FileOutputStream fos = new FileOutputStream(file); 
            // get filename to use on the server
            //String outputfile = this.getServletContext().getRealPath(filename);  // get path on the server
            FileOutputStream os = new FileOutputStream(file);

            // write bytes taken from uploaded file to target file
            int ch = is.read();
            while (ch != -1) {
                os.write(ch);
                ch = is.read();
            }
            os.close();
            out.println("<h3>File uploaded successfully!</h3>");
            response.sendRedirect("/HelloBigDataWorld/csvFields.html");
        } catch (Exception ex) {
            out.println("Exception -->" + ex.getMessage());
        } finally {
            out.close();

        }

    } // end of doPost()

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

} // end of UploadServlet
