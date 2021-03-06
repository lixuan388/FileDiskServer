package com.ecity.java.web.system.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import com.java.sql.table.MySQLTable;

@WebServlet("/Content/System/InitPassWord.json")
public class InitPassWordServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4341641892587343773L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		Map<String, String[]> params = request.getParameterMap();

		String UserID =params.get("id")==null?"":(String)(params.get("id")[0]);



		
		
		resp.setContentType("application/json;charset=utf-8"); 
		resp.setCharacterEncoding("UTF-8");  
		resp.setHeader("Cache-Control", "no-cache");
	    //使用request对象的getSession()获取session，如果session不存在则创建�?�?		

		net.sf.json.JSONObject ReturnJson = new net.sf.json.JSONObject();		
   
		if (!(Boolean)request.getSession().getAttribute("IsAdmin"))
		{
			ReturnJson.put("MsgID","-1");
			ReturnJson.put("MsgTest","无权限修改账号信息！");
		}
		else if (UserID.equals(""))
		{
			ReturnJson.put("MsgID","-1");
			ReturnJson.put("MsgTest","用户ID错误！");
		}
		else
		{
			String PassWord=DigestUtils.sha1Hex("888888").substring(0, 8).toUpperCase();			
			MySQLTable table=new MySQLTable("select AUs_ID,AUs_UserName,AUs_UserCode,aus_password,aus_STEdit,aus_deleteFlg,"
						+"aus_status,aus_user_lst,aus_User_ins,aus_date_lst,aus_date_ins"+
						" from aus_users where aus_status<>'D' and  aus_id='"+UserID+"'");
			try
			{
				table.Open();
//				System.out.println("open");
				if (!table.next())
				{		
					ReturnJson.put("MsgID","-1");
					ReturnJson.put("MsgTest","用户不存在");
				}
				else
				{
					table.updateString("aus_password", PassWord);
					table.updateString("aus_status", "E");
					table.updateString("aus_user_lst", (String)request.getSession().getAttribute("UserName"));
					table.updateDateTime("aus_date_lst", new Date());					
					
					table.updateRow();
					
			        ReturnJson.put("MsgID","1");
					ReturnJson.put("MsgTest","Success");
				}
			}
			finally
			{
				table.Close();
			}
		}
//		System.out.println(ReturnJson.toString());
        resp.getWriter().print(ReturnJson.toString());
        resp.getWriter().flush();	
	}

}
