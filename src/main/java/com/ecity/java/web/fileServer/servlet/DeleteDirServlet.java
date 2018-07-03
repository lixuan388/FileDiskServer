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
import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.java.sql.SQLConnect;

@WebServlet("/File/DeleteDir")
public class DeleteDirServlet extends HttpServlet {

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

    JSONObject ReturnJson = new JSONObject();

    DBTable table = new DBTable(SQLConnect.GetConnect(),
        "select * from adl_DirList where adl_ID=" + ID + "and adl_status<>'D'");
    DBTable FileTable = new DBTable(SQLConnect.GetConnect(),
        "select * from afl_FileList where afl_status<>'D' and afl_dirid=" + ID);
    DBTable DirTable = new DBTable(SQLConnect.GetConnect(),
        "select * from adl_DirList where adl_parentid=" + ID + "and adl_status<>'D'");
    try {
      FileTable.Open();
      DirTable.Open();

      table.Open();
      if (FileTable.next()) {
        ReturnJson.put("MsgID", -1);
        ReturnJson.put("MsgText", "此文件夹下还存在文件，不可删除！");
      } else if (DirTable.next()) {
        ReturnJson.put("MsgID", -1);
        ReturnJson.put("MsgText", "此文件夹下还存在文件，不可删除！");
      } else {

        if (table.next()) {
          table.UpdateValue("adl_status", "D");
          table.PostRow();
          ReturnJson.put("MsgID", 1);
          ReturnJson.put("MsgText", "文件夹删除成功！");
        } else {
          ReturnJson.put("MsgID", -1);
          ReturnJson.put("MsgText", "文件夹不存在！");

        }
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally

    {
      FileTable.CloseAndFree();
      DirTable.CloseAndFree();
      table.CloseAndFree();
    }

    response.getWriter().print(ReturnJson.toString());
    response.getWriter().flush();
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);
  }

}
