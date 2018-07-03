package com.ecity.java.web.fileServer.servlet;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.fileServer.fun.FileDigestUtil;
import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.ecity.java.web.fileServer.fun.GetGUIDString;
import com.java.sql.SQLConnect;

@WebServlet("/File/IteratorPath")
public class IteratorPathServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 4341641892587343773L;

  private String FileServerPath;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub

    // TODO Auto-generated method stub
    response.setContentType("application/json;charset=utf-8");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    JSONObject ReturnJson = new JSONObject();

    FileServerPath =(String) getServletContext().getAttribute("FileServerPath");
    SendMessage("---------开始更新文件数据！---------");
    iteratorPath(0, FileServerPath);
    SendMessage("---------文件数据更新完成！---------");
    ReturnJson.put("MsgID", 1);
    ReturnJson.put("MsgTest", "");


    FileWebPath.Init();
    response.getWriter().print(ReturnJson.toString());
    response.getWriter().flush();
  }

  // 用于遍历文件价
  public void iteratorPath(int PID, String dir) {
    File or = new File(dir);
    File[] files = or.listFiles();
    if (files != null) {

      for (File file : files) {
        if (file.isFile()) {
          SendMessage(dir + "\\" + file.getName());

          String FilePath = file.getAbsolutePath().replace(FileServerPath, "");
          FilePath = FilePath.replace("\\", "/");
          String FileExe = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
          String FileName = file.getName().substring(0, file.getName().lastIndexOf("."));
          String FileSha1 = FileDigestUtil.getSha1(file);
          String FileSize=""+file.length();
          UpdateFileInfo(PID, FilePath, FileName, FileExe, FileSha1,FileSize);
        } else if (file.isDirectory()) {
          SendMessage("【" + file.getName() + "】");
          int vPID = UpdateDirInfo(PID, file.getName());
          iteratorPath(vPID, file.getAbsolutePath());
        }
      }
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);
  }

  public void SendMessage(String message) {
//	      for(WebSocketServlet item: WebSocketServlet.webSocketSet){
//	      try {
//	          item.sendMessage("<div>"+message+"</div>");
//	      } catch (IOException e) {
//	          e.printStackTrace();
//	          continue;
//	      }
//	  }
  }

  public int UpdateDirInfo(int PID, String DirName) {
    DBTable table = new DBTable(SQLConnect.GetConnect(),
        "select * from adl_DirList where adl_ParentID=" + PID + " and adl_DirName='" + DirName + "'");
    try {
      table.Open();
      if (table.next()) {
        return table.getInt("adl_id");
      } else {
        table.insertRow();
        table.UpdateValue("adl_ParentID", PID);
        table.UpdateValue("adl_DirName", DirName);
        table.UpdateValue("adl_status", "I");
        table.PostRow();
        return UpdateDirInfo(PID, DirName);
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return 0;
    } finally {
      table.CloseAndFree();
    }
  }

  public void UpdateFileInfo(int DirID, String FilePath, String FileName, String FileExe, String FileSha1,String FileSize) {
    DBTable table = new DBTable(SQLConnect.GetConnect(),
        "select * from afl_FileList where afl_FilePath='" + FilePath + "' and afl_DirID='" + DirID + "'");
    try {
      table.Open();
      if (!table.next()) {
        table.insertRow();
        table.UpdateValue("afl_DirID", DirID);
        table.UpdateValue("afl_FileName", FileName);
        table.UpdateValue("afl_FileExe", FileExe);
        table.UpdateValue("afl_FilePath", FilePath);
        table.UpdateValue("afl_Sha", FileSha1);
        table.UpdateValue("afl_key", GetGUIDString.UUID());
        table.UpdateValue("afl_status", "I");
        table.UpdateValue("afl_date_lst",new Date());
        table.UpdateValue("afl_size",FileSize);
        table.PostRow();
      } else {
        if (table.getString("afl_Sha") != FileSha1) {

          table.UpdateValue("afl_Sha", FileSha1);
          table.PostRow();
        }
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
  }
}
