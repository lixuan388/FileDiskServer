package com.ecity.java.web.fileServer.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.ecity.java.json.JSONArray;
import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.java.sql.SQLConnect;

@WebServlet("/File/GetFileList")
public class GetFileListServlet extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = 4341641892587343773L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub

    // TODO Auto-generated method stub
    response.setContentType("application/json;charset=utf-8");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache");

    String id = request.getParameter("id") == null ? "-1" : request.getParameter("id");

    JSONObject FileLists = new JSONObject();
    JSONArray Dirs = new JSONArray();
    JSONArray Files = new JSONArray();
    JSONArray FileData = new JSONArray();

    DBTable table = new DBTable(SQLConnect.GetConnect(),
        "select adl_DirName,adl_ParentID,adl_id from adl_DirList where adl_ID=" + id
            + " and adl_status<>'D' order by adl_DirName");

    try {
      table.Open();
      if (table.next()) {
        FileLists.put("ParentID", table.getString("adl_ParentID"));
        FileLists.put("SID", table.getString("adl_id"));
        FileLists.put("DirName", table.getString("adl_DirName"));
        FileLists.put("FilePath", FileWebPath.Path(table.getString("adl_id")));
      } else {
        FileLists.put("ParentID", "0");
        FileLists.put("SID", "0");
        FileLists.put("DirName", "");
        FileLists.put("FilePath", "Home");
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


    table.SQL("select adl_DirName,adl_id from adl_DirList where adl_ParentID=" + id
        + " and adl_status<>'D' order by adl_DirName");
    try {
      table.Open();
      while (table.next()) {
        FileData.put(GetDirInfo(table));
      }

      table.SQL("select afl_id,afl_FileName,afl_FileExe,afl_date_lst,afl_user_lst,afl_size,afl_dirid from afl_FileList where afl_dirid=" + id
          + " and afl_status<>'D' order by afl_FileName");
      table.Open();
      while (table.next()) {
        FileData.put(GetFileInfo(table));
//			System.out.println(table.getString("afl_FileName"));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    table.Close();

    FileLists.put("MsgID", 1);
    FileLists.put("MsgText", "Success!");
    FileLists.put("Data", FileData);
//    System.out.println(FileLists.toString());
    response.getWriter().print(FileLists.toString());
    response.getWriter().flush();
  }

  public JSONObject GetDirInfo(DBTable table) {

    JSONObject FileInfo = new JSONObject();

    try {
      FileInfo.put("Name", table.getString("adl_DirName"));
      FileInfo.put("ID", table.getString("adl_id"));
      FileInfo.put("Exe","dir");
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return FileInfo;
  }

  public JSONObject GetFileInfo(DBTable table) {

    JSONObject FileInfo = new JSONObject();

    try {
      FileInfo.put("Name", table.getString("afl_FileName"));
      FileInfo.put("Exe", table.getString("afl_FileExe").toLowerCase());
      FileInfo.put("LastDate", WebFunction.FormatDate(WebFunction.Format_YYYYMMDDHHMMSS,(Date) table.GetValue("afl_date_lst")));
      FileInfo.put("LastUser", table.getString("afl_user_lst"));
      FileInfo.put("ID", table.getString("afl_id"));
      FileInfo.put("DirID", table.getString("afl_dirid"));
      
      int size=table.getInt("afl_size");
      String[] unit= {"B","KB","MB","G","T"};
      int unitindex=0;
      while (size>1024)
      {
        size=size/1024;
        unitindex=unitindex+1;
      }

      FileInfo.put("Size", ""+size+unit[unitindex]);
    } catch (JSONException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return FileInfo;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);
  }

}
