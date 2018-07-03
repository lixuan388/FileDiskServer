package com.ecity.java.web.fileServer.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.java.sql.SQLConnect;

@WebServlet("/CreateDir")
public class CreateDirServlet extends HttpServlet {
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

    String ID = request.getParameter("ID") == null ? "-1" : request.getParameter("ID");
    String Name = request.getParameter("Name") == null ? "" : request.getParameter("Name");

    Name = URLDecoder.decode(Name, "UTF-8");
//		System.out.println(Name);
    JSONObject ReturnJson = new JSONObject();
    DBTable table = new DBTable(SQLConnect.GetConnect(), "select * from adl_DirList where adl_ParentID=" + ID
        + " and adl_DirName='" + Name + "' and adl_status<>'D' order by adl_DirName");
    try {
      table.Open();
      if (table.next()) {
        WebFunction.WriteMsgText(response, -1, "文件夹名称已被使用！");
        return;
      } else {
        table.insertRow();
        try {
          table.UpdateValue("adl_ParentID", Integer.parseInt(ID));
        } catch (NumberFormatException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
          WebFunction.WriteMsgText(response, -1, "新建文件夹失败！<br>" + e.getMessage());
          return;
        }
        table.UpdateValue("adl_DirName", Name);
        table.UpdateValue("adl_status", "I");
        table.PostRow();
        FileWebPath.UpdatePath(ID);
        WebFunction.WriteMsgText(response, 1, "Success!");
        return;
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }

  }

//	public JSONObject GetDirInfo(MySQLTable table)
//	{
//		
//		net.sf.json.JSONObject FileInfo = new net.sf.json.JSONObject();            
//		
//
//		FileInfo.put("DirName",table.getString("adl_DirName"));
//		FileInfo.put("DirID",table.getString("adl_id"));
//		
//		return FileInfo;
//	}
//	
//	public JSONObject GetFileInfo(MySQLTable table)
//	{
//		
//		net.sf.json.JSONObject FileInfo = new net.sf.json.JSONObject();            
//		
//
//		FileInfo.put("FileName",table.getString("afl_FileName"));
//		FileInfo.put("FileExe",table.getString("afl_FileExe"));
//		FileInfo.put("FileID",table.getString("afl_id"));
//		
//		return FileInfo;
//	}
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);
  }

}
