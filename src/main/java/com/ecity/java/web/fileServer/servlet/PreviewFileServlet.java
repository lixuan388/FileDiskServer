package com.ecity.java.web.fileServer.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLConnect;

@WebServlet("/File/PreviewFile")
public class PreviewFileServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 4341641892587343773L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub

    String id = request.getParameter("id") == null ? "-1" : request.getParameter("id");

    DBTable table = new DBTable(SQLConnect.GetConnect(),
        "select afl_FileName,afl_fileexe,afl_filePath from afl_FileList where afl_id=" + id);
    try {
      table.Open();
      if (!table.next()) {
        response.sendError(404, "File Not Find!");
        return;
      }

      String UploadPath = getServletContext().getInitParameter("FileServerPath");

      String path = UploadPath + table.getString("afl_filePath");
      String FileName = table.getString("afl_FileName") + "." + table.getString("afl_fileexe");
      try {
        // path是指欲下载的文件的路径。
        File file = new File(path);
        if (!file.exists()) {
          response.sendError(404, "File Not Find!");
          return;
        }

        // 取得文件名。
        String filename = file.getName();
        // 取得文件的后缀名。
        String ext = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

        System.out.println("ext:" + ext);
        // 以流的形式下载文件。
        InputStream fis = new BufferedInputStream(new FileInputStream(path));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header

        String contentType = null;
        contentType = getServletContext().getInitParameter(ext);
        if ((contentType == null)) {
          contentType = "application/octet-stream";
        }
//	            response.addHeader("Content-Disposition", "attachment;filename=" +new String(FileName.getBytes("utf-8"),"iso-8859-1"));
        response.addHeader("Content-Disposition",
            "inline;filename=" + new String(FileName.getBytes("utf-8"), "iso-8859-1"));

        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");

        toClient.write(buffer);
        toClient.flush();
        toClient.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }

    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);
  }

}
