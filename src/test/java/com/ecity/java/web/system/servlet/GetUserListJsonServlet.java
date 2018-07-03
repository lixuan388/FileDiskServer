package com.ecity.java.web.system.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.sql.table.MySQLTable;

@WebServlet("/Content/System/GetUserList.json")
public class GetUserListJsonServlet extends HttpServlet {

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

		String id =request.getParameter("id")==null?"-1":request.getParameter("id");
 
//		System.out.println("id:"+id);
		
		net.sf.json.JSONArray UserLists = new net.sf.json.JSONArray();     
		
		
		MySQLTable table=new MySQLTable();

		try
		{
			if (!id.equals("-1"))
			{
				table.SQL("select aus_id,aus_username,aus_usercode,aus_password,aus_deleteFlg,aus_status,aus_STEdit from aus_users where aus_status<>'D' and aus_id="+id+"  order by aus_username");
			}
			else
			{
				table.SQL("select aus_id,aus_username,aus_usercode,aus_password,aus_deleteFlg,aus_status,aus_STEdit from aus_users where aus_status<>'D'  order by aus_username");
			}
			table.Open();
			while (table.next())
			{   
				net.sf.json.JSONObject UserInfo = new net.sf.json.JSONObject();    

				UserInfo.put("ID",table.getString("aus_id"));
				UserInfo.put("Name",table.getString("aus_username"));
				UserInfo.put("Code",table.getString("aus_usercode"));
				UserInfo.put("PassWord",table.getString("aus_password"));
				UserInfo.put("DeleteFlg",table.getString("aus_deleteFlg"));
				UserInfo.put("Status",table.getString("aus_status"));
				UserInfo.put("IsAdmin",table.getString("aus_STEdit"));
				
				UserLists.add(UserInfo);
	//			System.out.println(table.getString("afl_FileName"));
			}
		}
		finally
		{
			table.Close();
		}
		response.getWriter().print(UserLists.toString());
		response.getWriter().flush();		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
