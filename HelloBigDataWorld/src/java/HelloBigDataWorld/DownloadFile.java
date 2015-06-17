/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelloBigDataWorld;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author javatutorials.co.in
 */
@WebServlet("/download")
public class DownloadFile extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String fileDownload = (String) session.getAttribute("currentExport");
        String filePath = fileDownload;
        
        File file = new File(filePath);

        // set response headers (it is always better to write header's before writing to response stream)
        response.setContentType(getServletContext().getMimeType(file.getAbsolutePath()));
        // if Content-Disposition header value is set to attachment, file can be downloaded as attachment
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName());
        // if Content-Disposition header value is set to inline, the response is displayed in browser
        //response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName());

        // Copy input file's InputStream to response's OutputStream
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();

            int read = 0;
            final byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
